import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	static HashTable<Vertix> vertices;
	static int numberOfVertices;
	static int numberOfEdges;
	static LinkedList<Vertix> shortestPath = new LinkedList<>();
	Font customFontRegular = Font.loadFont(Main.class.getResourceAsStream("/Product Sans Regular.ttf"), 23);
	static Font customFontBold = Font.loadFont(Main.class.getResourceAsStream("/Product Sans Bold.ttf"), 23);

	BorderPane mainBP = new BorderPane();
	BorderPane rightBP = new BorderPane();
	static Pane p = new Pane();
	Label welcomeL = new Label("Welcome To Gaza Maps!");
	Label startingPointL = new Label("Choose A Starting Point:");
	static ComboBox<Vertix> startingPointCB = new ComboBox<Vertix>();
	Label destinationPointL = new Label("Choose A Destination Point:");
	static ComboBox<Vertix> destinationPointCB = new ComboBox<Vertix>();
	Button calculatePathBtn = new Button("Calculate Shortest Path");
	Label pathLengthL = new Label("Path Length:");
	Label estimatedTimeByCarL = new Label("Estimated Time By Car At 80km/hr:");
	Label numberOfCitiesVisitedL = new Label("Cities And Intersections Visited In path:");
	Label executionTimeL = new Label("Shortest Path Calculation Executed In: ms");
	Button addLocationBtn = new Button("Add New Location");
	Button removeLocationBtn = new Button("Remove A Location");
	Button chooseFileBtn = new Button("Choose Locations File");
//	Button changeMapBtn = new Button("Change Map");
	Button displayStreetsBtn = new Button("Display Streets Only");
	Button displayCitiesBtn = new Button("Display Cities Only");
//	Button savePathBtn = new Button("Save Path");
	Button showPathBtn = new Button("Show Path");

	Vertix previousStart; // used to check if the input is is for a source thats been calculated before

	static Image srcImage = new Image("location.png");
	static Image destImage = new Image("destination.png");

	static final double MAX_LONGITUDE = 34.576185;
	static final double MIN_LONGITUDE = 34.121228;
	static final double MAX_LATITUDE = 31.604837;
	static final double MIN_LATITUDE = 31.219004;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		pathLengthL.setMinWidth(400);
		pathLengthL.setMaxWidth(400);
		pathLengthL.setWrapText(true);
		estimatedTimeByCarL.setMinWidth(410);
		estimatedTimeByCarL.setMaxWidth(410);
		estimatedTimeByCarL.setWrapText(true);
		numberOfCitiesVisitedL.setMinWidth(400);
		numberOfCitiesVisitedL.setMaxWidth(400);
		numberOfCitiesVisitedL.setWrapText(true);
		executionTimeL.setMinWidth(400);
		executionTimeL.setMaxWidth(400);
		executionTimeL.setWrapText(true);

		mainBP.setLeft(p);
		Image mapImage = new Image("gaza.png");
		ImageView mapImageView = new ImageView(mapImage);
		mapImageView.setFitWidth(800);
		mapImageView.setFitHeight(800);

		p.getChildren().add(mapImageView);
		p.setMaxWidth(800);
		p.setMaxHeight(800);
		Scene scene = new Scene(mainBP, 1420, 800);
		scene.getStylesheets().add("style.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Algorithms Project 3  - Omar Kashour - 1210082");
		primaryStage.setResizable(false);
		Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
		primaryStage.show();
		File defaultFile = new File("citiesAndRoads.txt");
		if (readAndProcessFile(defaultFile)) { // if successful
			addCitiesToPane();
			fillStartingCB(startingPointCB);
			fillDestinationCB(destinationPointCB);
		}

		chooseFileBtn.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			File f = fc.showOpenDialog(primaryStage);
			boolean successful = readAndProcessFile(f); // if the method returns true then we have successfully read and
														// proccessed the file
			if (successful) {
				p.getChildren().clear();
				p.getChildren().add(mapImageView);
				startingPointCB.getItems().clear();
				destinationPointCB.getItems().clear();
//				printVerticesLatitudeAndLongitude();
//				printVerticesAndEdges();
//				printVerticesXandY();
				addCitiesToPane();
				fillStartingCB(startingPointCB);
				fillDestinationCB(destinationPointCB);
			}
		});

//		p.setOnMouseClicked(e -> {
//			double[] coordinates = convertXYtoCoordinates((int) e.getX(), (int) e.getY(), 800, 800);
//			System.out.println(coordinates[1] + " " + coordinates[0]);
//		});

		welcomeL.setStyle("-fx-font-size: 30;");
		mainBP.setRight(rightBP);
		rightBP.setAlignment(welcomeL, Pos.CENTER);
		rightBP.setMargin(welcomeL, new Insets(15));
		GridPane gp = new GridPane();

		gp.add(startingPointL, 0, 0);
		gp.add(destinationPointL, 0, 1);
		gp.add(startingPointCB, 1, 0);
		gp.add(destinationPointCB, 1, 1);
		gp.add(calculatePathBtn, 1, 2);
		gp.add(showPathBtn, 1, 3);
		gp.add(pathLengthL, 0, 3);
		gp.add(estimatedTimeByCarL, 0, 4);
		gp.add(numberOfCitiesVisitedL, 0, 5);
		gp.add(executionTimeL, 0, 6);
		gp.add(addLocationBtn, 0, 7);
		gp.add(removeLocationBtn, 1, 7);
		gp.add(chooseFileBtn, 0, 9);
		gp.add(displayStreetsBtn, 0, 8);
		gp.add(displayCitiesBtn, 1, 8);
		gp.setVgap(15);
		gp.setHgap(15);
		gp.setAlignment(Pos.CENTER);
		rightBP.setCenter(gp);
		rightBP.setMargin(gp, new Insets(15));
		rightBP.setStyle("-fx-background-color: #C2D9FF; -fx-border-color: #8E8FFA; -fx-border-width: 1px;");
		rightBP.setTop(welcomeL);

		showPathBtn.setOnAction(e -> {
			if (shortestPath == null || shortestPath.isEmpty()) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No calculation");
				alert.setContentText("The shortest path hasnt been calculated yet");
				alert.show();
				return;
			}
			Stage s = new Stage();
			BorderPane pathBP = new BorderPane();
			Label l = new Label("Exact Path");
			TextArea ta = new TextArea();
			ta.setText(shortestPath.toString());
			ta.setWrapText(true);
			ta.setEditable(false);
			pathBP.setTop(l);
			pathBP.setCenter(ta);
			pathBP.setMargin(ta, new Insets(15));
			pathBP.setMargin(l, new Insets(15));
			Scene pathScene = new Scene(pathBP);
			pathScene.getStylesheets().add("style.css");
			pathScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
			s.setScene(pathScene);
			s.setTitle("Path");
			s.show();
		});

		calculatePathBtn.setOnAction(e -> {
			if (startingPointCB.getValue() != null && destinationPointCB.getValue() != null) {
				Vertix startingPoint = startingPointCB.getValue();
				Vertix endingPoint = destinationPointCB.getValue();
				double timeBefore = System.currentTimeMillis();
				boolean needDijkstra = true;

				if (previousStart != null && previousStart.equals(startingPoint)) { // if we are starting from the same
																					// source
					for (Vertix v : shortestPath) { // check if we have calculated the shortest path to the ending point
													// before
						if (v.equals(endingPoint)) {
							needDijkstra = false;
							break;
						}

					}
				}

				if (needDijkstra) {
					shortestPath.clear();
					resetVertices();
					dijkstra(startingPoint, endingPoint);
					fillShortestPathList(endingPoint);
				}

				double result = System.currentTimeMillis() - timeBefore;
				executionTimeL.setText("Shortest Path Calculation Executed In: " + result + " ms");
				pathLengthL.setText("Path Length: " + String.format("%.2f", endingPoint.getDistance()) + " km");
				estimatedTimeByCarL.setText("Estimated Time By Car At 80km/hr: "
						+ String.format("%.2f", endingPoint.getDistance() / 80 * 60) + " mins");
				numberOfCitiesVisitedL.setText("Cities And Intersections Visited In Path: " + shortestPath.size());
				clearPath();
				drawPath(endingPoint); // this method draws the path and stops at the ending point
				previousStart = startingPoint; // save the previous source point
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Select points");
				alert.setContentText("Select a starting point and a destination point.");
				alert.show();
			}
		});

		displayStreetsBtn.setOnAction(e -> {
			if (vertices == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No vertices loaded yet");
				alert.setContentText("Please load a file containing cities and streets.");
				alert.show();
				return;
			}
			p.getChildren().clear();
			p.getChildren().add(mapImageView);
			addStreetsToPane();
		});
		displayCitiesBtn.setOnAction(e -> {
			if (vertices == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No vertices loaded yet");
				alert.setContentText("Please load a file containing cities and streets.");
				alert.show();
				return;
			}
			p.getChildren().clear();
			p.getChildren().add(mapImageView);
			addCitiesToPane();
		});

		addLocationBtn.setOnAction(e -> {
			Stage locationStage = new Stage();
			BorderPane locationBP = new BorderPane();
			Label locationNameL = new Label("Location Name:");
			TextField locationNameTF = new TextField();
			Label latitudeL = new Label("Latitude Degrees:");
			Label longitudeL = new Label("Longitude Degrees:");
			Label nearbyVertixL = new Label("Select A Nearby City Or Street:");
			TextField latitudeTF = new TextField();
			TextField longitudeTF = new TextField();
			Button addLocationBtn = new Button("Add Location");
			GridPane locationGP = new GridPane();
			ComboBox<Vertix> locationCB = new ComboBox<Vertix>();

			fillNearbyCB(locationCB);

			locationGP.add(locationNameL, 0, 0);
			locationGP.add(latitudeL, 0, 1);
			locationGP.add(longitudeL, 0, 2);
			locationGP.add(nearbyVertixL, 0, 3);
			locationGP.add(locationNameTF, 1, 0);
			locationGP.add(latitudeTF, 1, 1);
			locationGP.add(longitudeTF, 1, 2);
			locationGP.add(locationCB, 1, 3);
			locationGP.add(addLocationBtn, 1, 4);
			locationBP.setCenter(locationGP);
			locationBP.setMargin(locationGP, new Insets(15));
			locationGP.setHgap(15);
			locationGP.setVgap(15);
			Scene locationScene = new Scene(locationBP);
			locationScene.getStylesheets().add("style.css");
			locationScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
			locationStage.setScene(locationScene);
			locationStage.setTitle("Add a new location");
			locationStage.show();

			addLocationBtn.setOnAction(e1 -> {
				try {
					String data = locationNameTF.getText().trim();
					double latitude = Double.parseDouble(latitudeTF.getText().trim());
					double longitude = Double.parseDouble(longitudeTF.getText().trim());
					Vertix v = new Vertix(data, latitude, longitude);
					Vertix nearbyVertix = locationCB.getValue();
					double distance = haversineFormula(latitude, nearbyVertix.getLatitude(), longitude,
							nearbyVertix.getLongitude());

					if ((latitude > MAX_LATITUDE || latitude < MIN_LATITUDE)
							|| (longitude > MAX_LONGITUDE || longitude < MIN_LONGITUDE || latitude > 31.593224 || latitude < 31.217910 || longitude < 34.218421 )) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Invalid Coordinates");
						alert.setContentText("Invalid Coordinates, outside of Gaza or map boundries.");
						alert.show();
						return;
					}

					Edge edge1 = new Edge(v, nearbyVertix, distance);
					Edge edge2 = new Edge(nearbyVertix, v, distance);
					v.getEdges().add(edge1);
					nearbyVertix.getEdges().add(edge2);
					vertices.add(v);
					double[] xy = convertCoordinatesToXY(latitude, longitude, 800, 800);
					v.setX(xy[0]);
					v.setY(xy[1]);
					p.getChildren().clear();
					p.getChildren().add(mapImageView);
					startingPointCB.getItems().clear();
					destinationPointCB.getItems().clear();
					addCitiesToPane();
					fillStartingCB(startingPointCB);
					fillDestinationCB(destinationPointCB);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Added location");
					alert.setContentText("Location has been successfully added.");
					alert.show();
				} catch (Exception ex) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Wrong or missing values");
					alert.setContentText("You need to fill all fields correctly");
					alert.show();
				}
			});
		});

		removeLocationBtn.setOnAction(e -> {
			Stage removeLocationStage = new Stage();
			BorderPane removeBP = new BorderPane();
			Label removeL = new Label("Select A Location To Be Removed:");
			ComboBox<Vertix> removeCB = new ComboBox<Vertix>();
			fillStartingCB(removeCB); // fill the remove cb with the same elements as the sources cb
			Button removeBtn = new Button("Remove Location");
			removeBtn.setOnAction(e1 -> {
				try {
					Vertix v = removeCB.getValue();
					if (v == null) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("No location select");
						alert.setContentText("Please select a location to delete from the map.");
						alert.show();
						return;
					}
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Removing location");
					alert.setContentText("Are you sure you want to delete '" + v + "' ?");
					delete = true;
					alert.showAndWait().ifPresent(r -> {
						if (r != ButtonType.OK) {
							delete = false;
						}
					});

					if (!delete)
						return;

					vertices.remove(v);
					for (HashNode<Vertix> n : vertices.getTable()) {
						if (n.getData() != null)
							for (int i = 0; i < n.getData().getEdges().size(); i++) {
								if (n.getData().getEdges().get(i).getEndVertix().equals(v)) {
									n.getData().getEdges().remove(i);
								}
							}
					}
					p.getChildren().clear();
					p.getChildren().add(mapImageView);
					startingPointCB.getItems().clear();
					destinationPointCB.getItems().clear();
					addCitiesToPane();
					fillStartingCB(startingPointCB);
					fillDestinationCB(destinationPointCB);
				} catch (Exception ex) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("No location chosen");
					alert.setContentText("You need to choose a location to remove");
					alert.show();
				}
			});
			GridPane removeGP = new GridPane();
			removeGP.add(removeL, 0, 0);
			removeGP.add(removeCB, 1, 0);
			removeGP.add(removeBtn, 1, 1);
			removeGP.setVgap(15);
			removeGP.setHgap(15);
			Scene removeScene = new Scene(removeBP);
			removeScene.getStylesheets().add("style.css");
			removeScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
			removeBP.setCenter(removeGP);
			removeBP.setMargin(removeGP, new Insets(15));
			removeLocationStage.setScene(removeScene);
			removeLocationStage.show();
		});

	}

	private void fillShortestPathList(Vertix endingPoint) {
		Vertix v = endingPoint;
		while (v != null) { // 
			shortestPath.addFirst(v); // flip the result. 
			v = v.getPrevious();
		}		
	}

	static boolean delete = true; // for confirmation alert of removing a location

	private void resetVertices() {
		for (HashNode<Vertix> n : vertices.getTable()) {
			if (n.getData() != null) {
				n.getData().setDistance(Double.MAX_VALUE);
				n.getData().setPrevious(null);
			}
		}

	}

	private static void clearPath() {
		p.getChildren().removeIf(node -> node instanceof Line);
		p.getChildren().removeIf(node -> node instanceof Polygon);
	}


	private void drawPath(Vertix endingPoint) {
		if (shortestPath.size() > 1 && shortestPath != null) {
			for (int i = 0; i < shortestPath.size() - 1; i++) {
				Vertix v1 = shortestPath.get(i);
				double startX = v1.getX();
				double startY = v1.getY();
				Vertix v2 = shortestPath.get(i + 1);
				double endX = v2.getX();
				double endY = v2.getY();
				Line line = new Line(startX, startY, endX, endY);

				// create arrow
					double angle = Math.atan2((endY - startY), (endX - startX));
					double x1 = endX - 15 * Math.cos(angle - Math.toRadians(15));
					double x2 = endX - 15 * Math.cos(angle + Math.toRadians(15));
					double y1 = endY - 15 * Math.sin(angle - Math.toRadians(15));
					double y2 = endY - 15 * Math.sin(angle + Math.toRadians(15));
					Polygon arrowhead = new Polygon(endX, endY, x1, y1, x2, y2);
					arrowhead.setFill(Color.RED);
					arrowhead.setStroke(Color.BLACK);
				
				

		

				line.setFill(Color.RED);
				line.setStrokeWidth(2);
				p.getChildren().add(line);
//				if(i < shortestPath.size()-2)
				p.getChildren().add(arrowhead);

				if (v2.equals(endingPoint))
					return;
			}
		}
	}

	private void fillStartingCB(ComboBox<Vertix> startingPointCB) {
		for (HashNode<Vertix> e : vertices.getTable()) {
			if (e.getData() != null && !isStreet(e.getData()))
				startingPointCB.getItems().add(e.getData());
		}

	}

	private void fillDestinationCB(ComboBox<Vertix> destinationPointCB) {
		for (HashNode<Vertix> e : vertices.getTable()) {
			if (e.getData() != null && !isStreet(e.getData()))
				destinationPointCB.getItems().add(e.getData());
		}

	}

	private void fillNearbyCB(ComboBox<Vertix> nearbyCB) {
		for (HashNode<Vertix> e : vertices.getTable()) {
			if (e.getData() != null)
				nearbyCB.getItems().add(e.getData());
		}
	}

	public static Vertix getVertixById(String name) {
		return vertices.getKey(new Vertix(name));
	}

	public static void dijkstra(Vertix source, Vertix destination) { 
		MinHeap<Vertix> heap = new MinHeap<>(numberOfVertices);
		source.setDistance(0);
		heap.add(source);
		while (!heap.isEmpty()) {
			Vertix current = heap.removeMin(); 
			if (current.equals(destination)) { 
				break;
			}
			for (Edge edge : current.getEdges()) { 
				Vertix nextVertex = edge.getEndVertix();
				double newDistance = current.getDistance() + edge.getWeight(); // edge weight is distance
				if (newDistance < nextVertex.getDistance()) {
					heap.remove(nextVertex); // remove the vertex from heap to update its distance. 
					nextVertex.setDistance(newDistance);
					nextVertex.setPrevious(current); // set the previous vertex for shortest path
					heap.add(nextVertex); // add the updated vertex back. 
				}
			}
		}
	}

	public static void printVerticesAndEdges() {
		for (HashNode<Vertix> n : vertices.getTable()) {
			if (n.getData() != null)
				System.out.println("Vertix: [" + n.getData() + "] Edges: " + n.getData().getEdges());
		}
	}

	public static void printVerticesLatitudeAndLongitude() {
		for (HashNode<Vertix> n : vertices.getTable()) {
			if (n.getData() != null)
				System.out.println("Vertix: [" + n.getData() + "] Latitude: " + n.getData().getLatitude()
						+ " Longitude: " + n.getData().getLongitude());
		}
	}

	public static void printVerticesXandY() {
		for (HashNode<Vertix> n : vertices.getTable()) {
			if (n.getData() != null) {
				double[] coordinates = convertCoordinatesToXY(n.getData().getLatitude(), n.getData().getLongitude(),
						800, 800);
				System.out.println(n.getData() + " X:" + coordinates[0] + " Y:" + coordinates[1]);

			}
		}
	}

	public static void addCitiesToPane() {
		for (HashNode<Vertix> n : vertices.getTable()) {
			if (n.getData() != null && !isStreet(n.getData())) {
				Label l = new Label(n.getData().toString());
				Circle c = new Circle(5);
				c.setFill(Color.web("#7752FE"));
				c.setCenterX(n.getData().getX());
				c.setCenterY(n.getData().getY());
				c.setOnMouseMoved(e -> {
					if (c.getRadius() == 5)
						c.setRadius(c.getRadius() * 1.35); // grow the circle by 35%
				});
				c.setOnMouseExited(e -> {
					if (c.getRadius() != 5)
						c.setRadius(5);
				});

				c.setOnMouseClicked(e -> {
					System.out.println(n.getData().toString());
					ContextMenu menu = new ContextMenu();
					MenuItem src = new MenuItem("Set As Source");
					MenuItem dest = new MenuItem("Set As Destination");
					menu.getItems().addAll(src, dest);
					menu.show(c, c.getCenterX() + 30, c.getCenterY());
					src.setOnAction(e2 -> {
						startingPointCB.setValue(n.getData());
						clearPath();
					});
					dest.setOnAction(e3 -> {
						destinationPointCB.setValue(n.getData());
						clearPath();
					});
				});

				l.setLayoutX(n.getData().getX() - (l.getText().length() / 2)); // try to center the label
				l.setLayoutY(n.getData().getY() + 5);
				l.setStyle("-fx-font-size: 12;");
				l.setAlignment(Pos.CENTER);
				p.getChildren().add(l);
				p.getChildren().add(c);

			}
		}

		for (int i = 0; i < p.getChildren().size(); i++) { // move the circles forward so they dont get covered by
															// labels
			if (p.getChildren().get(i) instanceof Circle) {
				p.getChildren().get(i).toFront();
			}
		}
	}

	public static void addStreetsToPane() {
		for (int i = 0; i < vertices.getTable().length - 1; i++) { // draw the street lines
			for (int j = 0; j < vertices.getTable().length - 1; j++) {
				if (vertices.getTable()[i].getData() != null && vertices.getTable()[j].getData() != null) {
					Vertix v1 = vertices.getTable()[i].getData();
					Vertix v2 = vertices.getTable()[j].getData();
					if (v1 != v2) {
						if (isStreet(v1) && isStreet(v2)) {
							for (Edge e1 : v1.getEdges()) {
								if (e1.getEndVertix() == v2) { // if both street are connected to each other, draw a
																// line
									Line line = new Line(v1.getX(), v1.getY(), v2.getX(), v2.getY());
									p.getChildren().addAll(line);
								}
							}
						}
					}
				}
			}
		}
		for (HashNode<Vertix> n : vertices.getTable()) { // draw the street circles
			if (n.getData() != null && isStreet(n.getData())) {
				Label l = new Label(n.getData().toString());
				l.setStyle("-fx-font-size: 16;");
				l.setAlignment(Pos.CENTER);
				l.setOpacity(0);
				Circle c = new Circle(3);
				c.setFill(Color.web("#7752FE"));
				c.setCenterX(n.getData().getX());
				c.setCenterY(n.getData().getY());
				l.setLayoutX(n.getData().getX() - 20);
				l.setLayoutY(n.getData().getY() - 20);
				c.setOnMouseMoved(e -> {
					if (c.getRadius() == 3) {
						c.setRadius(c.getRadius() * 1.35); // grow the circle by 35%
						l.setOpacity(100);
					}
				});
				c.setOnMouseExited(e -> {
					if (c.getRadius() != 3) {
						c.setRadius(3);
						l.setOpacity(0);
					}
				});

				c.setOnMouseClicked(e -> {
					System.out.println(n.getData().toString());
				});
				p.getChildren().add(l);
				p.getChildren().add(c);

			}
		}

		for (int i = 0; i < p.getChildren().size(); i++) { // move the circles forward so they dont get covered by
															// labels
			if (p.getChildren().get(i) instanceof Circle) {
				p.getChildren().get(i).toFront();
			}
		}
	}

	public static boolean isStreet(Vertix v) {
		return v.getData().contains("Street");
	}

	public static double[] convertCoordinatesToXY(double latitude, double longitude, int mapWidth, int mapHeight) {
		double[] coordinates = new double[2];
		// calculate the scale for longitude and latitude
		double longitudeScale = (MAX_LONGITUDE - MIN_LONGITUDE) / mapWidth;
		double latitudeScale = (MAX_LATITUDE - MIN_LATITUDE) / mapHeight;

		// calculate pixel positions
		double x = (longitude - MIN_LONGITUDE) / longitudeScale;
		double y = (MAX_LATITUDE - latitude) / latitudeScale;

		coordinates[0] = x;
		coordinates[1] = y;

		return coordinates;
	}

	public static double haversineFormula(double lat1, double lat2, double lon1, double lon2) {
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);


		double dlon = lon2 - lon1;
		double dlat = lat2 - lat1;
		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);

		double c = 2 * Math.asin(Math.sqrt(a));
		double r = 6371;

		return (c * r);
	}

	public boolean readAndProcessFile(File f) {
		try {
			Scanner sc = new Scanner(f);

			numberOfVertices = sc.nextInt();
			numberOfEdges = sc.nextInt();
			vertices = new HashTable<Vertix>(numberOfVertices * 2 + 1);

			int numberOfLines = numberOfEdges + numberOfVertices;

			int i = 0;
			int j = 0;
			while (i++ < numberOfVertices) { // add the vertices to the hashtable
				String data = sc.next();
				double latitude = sc.nextDouble();
				double longitude = sc.nextDouble();
				Vertix v = new Vertix(data, latitude, longitude);
				double[] coordinates = convertCoordinatesToXY(latitude, longitude, 800, 800);
				v.setX(coordinates[0]);
				v.setY(coordinates[1]);
				vertices.add(v);
				numberOfLines--;
			}
			System.out.println("Added All Cities.");

			while (j++ < numberOfEdges) { // add the edges
				String startVertixName = sc.next();
				String endVertixName = sc.next();
				Vertix startVertix = getVertixById(startVertixName);
				Vertix endVertix = getVertixById(endVertixName);
//				double distance = euclideanDistance(startVertix.getLatitude(),endVertix.getLatitude(),startVertix.getLongitude(),endVertix.getLongitude()); // euclidean distance formula
				double distance = haversineFormula(startVertix.getLatitude(), endVertix.getLatitude(),
						startVertix.getLongitude(), endVertix.getLongitude()); // Haversine formula

				Edge edge = new Edge(startVertix, endVertix, distance);
				startVertix.getEdges().add(edge);
				if (startVertixName.contains("Street") || endVertixName.contains("Street")) { // if one of the vertices
																								// is a street, connect
																								// them both ways
					Edge edge2 = new Edge(endVertix, startVertix, distance);
					endVertix.getEdges().add(edge2);
				}
			}
			System.out.println("Added All Edges.");
			return true;
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("File not found");
			alert.setContentText("Please choose a file.");
			alert.show();
			return false;
		} catch (NullPointerException e1) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("File not found");
			alert.setContentText("Please choose a file.");
			alert.show();
			return false;
		} catch (Exception e2) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid File");
			alert.setContentText(
					"Invalid file formatting,To represent a map in a file, list the number of vertices and edges, then list the vertices (index followed by Latitude and longitude), then list the edges (pairs of vertices)");
			alert.show();
			return false;
		}
	}

	public static double[] convertXYtoCoordinates(int x, int y, int mapWidth, int mapHeight) {
		double[] coordinates = new double[2];
		// calculate the scale for longitude and latitude
		double longitudeScale = (MAX_LONGITUDE - MIN_LONGITUDE) / mapWidth;
		double latitudeScale = (MAX_LATITUDE - MIN_LATITUDE) / mapHeight;

		// calculate pixel positions
		double longitude = (x * longitudeScale) + MIN_LONGITUDE;
		double latitude = MAX_LATITUDE - (y * latitudeScale);

		coordinates[0] = longitude;
		coordinates[1] = latitude;

		return coordinates;
	}

	public static double euclideanDistance(double lat1, double lat2, double lon1, double lon2) {
		double distance = Math.sqrt(Math.pow(Math.abs(lat2 - lat1), 2) + Math.pow(Math.abs(lon2 - lon1), 2));
		return distance * 111; // each lat/long degree is about 111km
	}

}
