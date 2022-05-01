package controlador;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Asistencia;
import model.AsistenciaDAO;

public class AsistenciaController{
	//Objecte per gestionar la persistència de les dades
	private AsistenciaDAO asistenciaDAO;
	//Objecte per gestionar el objecte actual
	private Asistencia asistencia = null;
	//indicador de nou registre
	private boolean nouRegistre = false;
	//objecte per afegir les files de la taula
	private ObservableList<Asistencia> asistenciaData;

	//Elements gràfics de la UI
	@FXML
	private AnchorPane anchorPane;
	private Stage ventana;
	@FXML private TextField idTextField;
	@FXML private DatePicker fechaEntradaDatePicker;
	@FXML private TextField horaEntradaTextField;
	@FXML private DatePicker fechaSalidaDatePicker;
	@FXML private TextField horaSalidaTextField;

	@FXML private TableView<Asistencia> asistenciaTable;
	@FXML private TableColumn<Asistencia, Integer> idColumn;
	@FXML private TableColumn<Asistencia, LocalDateTime> entradaColumn;
	@FXML private TableColumn<Asistencia, LocalDateTime> salidaColumn;

	private ValidationSupport vs;

	// Crear conexion a la base de datos
	public void setConexionBD(Connection conexionBD) {	
		//Crear objecte DAO de asistencia
		asistenciaDAO = new AsistenciaDAO(conexionBD);
		
		// Aprofitar per carregar la taula de asistencia (no ho podem fer al initialize() perque encara no tenim l'objecte conexionBD)
		// https://code.makery.ch/es/library/javafx-tutorial/part2/
		asistenciaData = FXCollections.observableList(asistenciaDAO.getAsistenciaList());
		asistenciaTable.setItems(asistenciaData);
	}

	/**
	 * Inicialitza la classe. JAVA l'executa automàticament després de carregar el fitxer fxml
	 */
	@FXML private void initialize() {
		idColumn.setCellValueFactory(new PropertyValueFactory<Asistencia,Integer>("id"));
		entradaColumn.setCellValueFactory(new PropertyValueFactory<Asistencia,LocalDateTime>("fechaEntrada"));
		salidaColumn.setCellValueFactory(new PropertyValueFactory<Asistencia,LocalDateTime>("fechaSalida"));

		// Quan l'usuari canvia de linia executem el métode mostrarPersona
		asistenciaTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> mostrarAsistencia(newValue));

		//Validació dades
		//https://github.com/controlsfx/controlsfx/issues/1148
		//produeix error si no posem a les VM arguments això: --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED
		vs = new ValidationSupport();
		vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatori"));
		vs.registerValidator(horaEntradaTextField, true, Validator.createEmptyValidator("Hora d'entrada obligatoria"));
		vs.registerValidator(horaSalidaTextField, true, Validator.createEmptyValidator("Hora de sortida obligatoria"));
		vs.registerValidator(fechaEntradaDatePicker, true, Validator.createEmptyValidator("Data d'entrada obligatoria"));
		vs.registerValidator(fechaSalidaDatePicker, true, Validator.createEmptyValidator("Data de sortidaobligatoria"));
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML private void onKeyPressedId(KeyEvent e) throws IOException {
		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
			//Comprovar si existeix la asistencia indicada en el control idTextField
			asistencia = asistenciaDAO.find(Integer.parseInt(idTextField.getText()));
			mostrarAsistencia(asistencia);
			//seleccionar la fila de la taula asociada al codi introduit
			asistenciaTable.getSelectionModel().select(asistencia);
			asistenciaTable.refresh();
		}
	}
	 
	@FXML private void onActionGuardar(ActionEvent e) throws IOException {
		
		//verificar si les dades són vàlides
		if(isDatosValidos()){
			if(nouRegistre){
				asistencia = new Asistencia(
					Integer.parseInt(idTextField.getText()), 
					null, 
					null
				);
				asistenciaData.add(asistencia);

			}else{
				//modificació registre existent
				asistencia.setFechaEntrada(null);
				asistencia.setFechaSalida(null);
			}

			asistenciaDAO.save(asistencia);
			limpiarFormulario();

			asistenciaTable.refresh();
			asistenciaDAO.showAll();
		}
	}

	@FXML private void onActionEliminar(ActionEvent e) throws IOException {
		if(isDatosValidos()){
			// Mostrar missatge confirmació
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Vol esborrar la asistencia " + idTextField.getText() + "?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				if(asistenciaDAO.delete(Integer.parseInt(idTextField.getText()))){ 
					asistenciaData.remove(asistenciaTable.getSelectionModel().getSelectedIndex());

					limpiarFormulario();
					asistenciaDAO.showAll();
				}
			}
		}
	}

	@FXML private void onActionSortir(ActionEvent e) throws IOException {
		sortir();
		//tancar el formulari
		((BorderPane)anchorPane.getParent()).setCenter(null);
	}

	public void sortir(){
		asistenciaDAO.showAll();
	}

	private boolean isDatosValidos() {

		//Comprovar si totes les dades són vàlides
		if (vs.isInvalid()) {
			String errors = vs.getValidationResult().getMessages().toString();
			// Mostrar finestra amb els errors
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(ventana);
			alert.setTitle("Camps incorrectes");
			alert.setHeaderText("Corregeix els camps incorrectes");
			alert.setContentText(errors);
			alert.showAndWait();
		
			return false;
		}

		return true;

	}

	private void mostrarAsistencia(Asistencia asistencia) {
		if(asistencia != null){ 
			//llegir asistencia (posar els valors als controls per modificar-los)
			nouRegistre = false;
			idTextField.setText(String.valueOf(asistencia.getId()));
			fechaEntradaDatePicker.setValue(null);
			horaEntradaTextField.setText("");
			fechaSalidaDatePicker.setValue(null);
			horaSalidaTextField.setText("");

		}else{ 
			//nou registre
			nouRegistre = true;
			//idTextField.setText(""); no hem de netejar la PK perquè l'usuari ha posat un valor
			fechaEntradaDatePicker.setValue(null);
			horaEntradaTextField.setText("");
			fechaSalidaDatePicker.setValue(null);
			horaSalidaTextField.setText("");
		}
	}
	
	private void limpiarFormulario(){
		idTextField.setText("");
		fechaEntradaDatePicker.setValue(null);
		horaEntradaTextField.setText("");
		fechaSalidaDatePicker.setValue(null);
		horaSalidaTextField.setText("");
	}
}
