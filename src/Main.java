import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
	// sisu määran neile seadistapaigutus () meetodis
	GridPane paigutus;
	Stage stage2;
	Scene vaade;
	ChoiceBox cb;
	ChoiceBox cb2;
	ChoiceBox cb3;
	ArrayList aasta;
	int algus;
	String piirkond;
	int lopp;

	@Override
	public void start(Stage primaryStage) throws Exception {

		seadistapaigutus();

	}

	private void seadistapaigutus() {
		// seadistan akna ja muud vajalikud asjad
		paigutus = new GridPane();
		vaade = new Scene(paigutus, 400, 200);
		paigutus.setHgap(10);
		paigutus.setVgap(10);
		paigutus.setPadding(new Insets(10, 10, 10, 10));
		stage2 = new Stage();
		stage2.setScene(vaade);
		stage2.setTitle("Kinnisvarahinnad piirkonnas 1 ja 2");
		paigutus.setGridLinesVisible(false);
		stage2.show();

		// loon dropdown'i, mille abil saab valida kahe piirkonna vahel + lisan labeli, mis aitab seletada, mida saab choicebox'ist valida
		Label nimi1 = new Label("Vali piirkond");
		paigutus.add(nimi1, 0, 0);
		cb = new ChoiceBox(FXCollections.observableArrayList("Piirkond 1", "Piirkond 2"));
		paigutus.add(cb, 1, 0);

		//genereerin aastate arraylisti perioodi valiku choiceboxide jaoks
		aasta = new ArrayList();
		for (int i = 2000; i < 2017; i++) {
			aasta.add(i);
		}

		// loon perioodi alguse choiceboxi + lisan labeli,  mis aitab seletada, mida saab choicebox'ist valida
		Label nimi2 = new Label("Vali perioodi algus");
		paigutus.add(nimi2, 0, 1);
		cb2 = new ChoiceBox(FXCollections.observableArrayList(aasta));
		paigutus.add(cb2, 1, 1);

		// loon perioodi lõpu choiceboxi + lisan labeli,  mis aitab seletada, mida saab choicebox'ist valida
		Label nimi3 = new Label("Vali perioodi lõpp");
		paigutus.add(nimi3, 0, 2);
		cb3 = new ChoiceBox(FXCollections.observableArrayList(aasta));
		paigutus.add(cb3, 1, 2);

		//Selleks, et programm reageeriks kliklie lisan listeneri. Salvestan uued väärtused piirkonna, perioodi alguse ja perioodi
		//lõpu kohta muutujatesse. Kasutan uusi väärtusi graafiku joonistamisel
		cb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			piirkond = (String) newValue;
		});

		cb2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			algus= (int) newValue;
		});

		cb3.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			lopp = (int) newValue;
		});

		//Lisan nupu, mida vajutades vahetab programm akna ning joonistatakse graafik
		Button nupp = new Button();
		nupp.setText("Joonista graafik");
		paigutus.add(nupp, 3, 5);

		nupp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// kui valitud perioodi algus on perioodi lõpust hilisem, siis annab programm veateate
				if (lopp<algus) {
					annaveateade ();
				} else {
					// kui perioodi algus ja lõpp on loogiliselt sisestatud, joonistatakse graafik
					joonistagraafik();
				}

			}
		});

	}
	// Lisan veateate akna
	private void annaveateade() {
		StackPane layout = new StackPane();
		Scene vaade2 = new Scene(layout,600, 200);
		stage2.setScene(vaade2);
		Label veateade = new Label("Perioodi sisestamisel tekkis viga. Perioodi lõpp on perioodi algusest varasem.");
		layout.getChildren().add(veateade);
		layout.setAlignment(veateade, Pos.TOP_CENTER);

		//lisan nupu, mille vajutamisel liigutakse tagasi eelmisele aknale, kust saab uuesti valida perioodi alguse ja lõpu
		Button nupp2 = new Button();
		nupp2.setText("Sisesta perioodi algus ja lõpp uuesti");
		layout.getChildren().add(nupp2);
		layout.setAlignment(nupp2, Pos.CENTER);
		stage2.show();
		nupp2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage2.setScene(vaade);
				stage2.show();
			}
		});

	}
	//seadistan graafiku joonistamise akna, paigutuse ja muu vajaliku
	private void joonistagraafik() {
		StackPane layout = new StackPane();
		Scene vaade3 = new Scene(layout,600, 600);
		stage2.setScene(vaade3);
		// x-teljel kasutan väärtustena kasutaja poolt valitud perioodi algust ja lõppu
		final NumberAxis xtelg = new NumberAxis(algus, lopp, 1);
		final NumberAxis ytelg = new NumberAxis();
		xtelg.setLabel("Aasta");
		ytelg.setLabel("Hinnakasv protsentides");
		final LineChart<Number,Number> graafik = new LineChart<>(xtelg,ytelg);
		layout.getChildren().add(graafik);
		graafik.setData(valiandmed());
		graafik.setTitle("Kinnisvarahindade kasv: "+ piirkond);
		stage2.setTitle("Kinnisvarahinnad piirkonnas 1 ja 2");

	}
	//andmete valiku meetod
	private ObservableList<XYChart.Series<Number,Number>> valiandmed() {
		ObservableList<XYChart.Series<Number, Number>> andmed = FXCollections.observableArrayList();
		XYChart.Series<Number, Number> piirkond1 = new XYChart.Series<Number, Number>();
		XYChart.Series<Number, Number> piirkond2 = new XYChart.Series<Number, Number>();
		piirkond1.setName("Hinnakasv piirkonnas 1");
		piirkond2.setName("Hinnakasv piirkonnas 2");
     // kuna ma veel andmebaasist andmeid võtta ei oska, genereerin hüpoteetilised hinnakasvud, mida saaks graafikule kanda
		double vaartus1 = 5;
		double vaartus2 = -2;

		for (int i = algus; i <= lopp; i++) {
			piirkond1.getData().add(new XYChart.Data<Number, Number>(i, vaartus1));
			vaartus1 = vaartus1 + Math.random() * 2.5 - Math.random() * 0.5;
			piirkond2.getData().add(new XYChart.Data<Number, Number>(i, vaartus2));
			vaartus2 = vaartus2 + Math.random() * 3.5 - Math.random() * 1;
		}
		// graafikule kannan selle piirkonna hinnakasvud, mille kasutaja valis
		if (piirkond.equals("Piirkond 1")) {
			andmed.add(piirkond1);
		}
		else {
			andmed.add(piirkond2);
		}
		return andmed;
	}


}