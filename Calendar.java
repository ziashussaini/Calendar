package project4;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Observable;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Calendar extends Application
{
   private BorderPane containerPane = new BorderPane();
   private LocalDateTime date = LocalDateTime.now();
   private String appointmentFile = "src/project4/appointmentFile.csv";
   private Scene scene;
   private Insets in = new Insets(30, 0, 0, 0);
   private Insets navLeft = new Insets(0, 0, 25, 0);
   private Insets navRight = new Insets(0, 25, 0, 0);
   //Top pane   
   private HBox navPane = new HBox();
   private HBox monthAndYear = new HBox(10);
   private Text month = new Text();
   private Text year = new Text();
   private HBox buttonNav = new HBox(10);
   final private Button leftBtn = new Button("<");
   final private Button yearBtn = new Button("Year");
   final private Button todayBtn = new Button("Today");
   final private Button rightBtn = new Button(">");
   //Set up MonthPane
   private StackPane sPane;
   private final String[] days = {
       "S",
       "M",
       "T",
       "W",
       "T",
       "F",
       "S"
   };
   private final String[] fullDays = {
       "Sunday",
       "Monday",
       "Tuesday",
       "Wednesday",
       "Thursday",
       "Friday",
       "Saturday"
   };
   //Fill up month
   private int plug = 0;
   private int currentYear;
   private int currentMonth;
   private String currentMonthName;
   private int currentDay;
   private int previousMonth;
   private int postMonth;
   private final String[] months = {
       "JANUARY",
       "FEBRUARY",
       "MARCH",
       "APRIL",
       "MAY",
       "JUNE",
       "JULY",
       "AUGUST",
       "SEPTEMBER",
       "OCTOBER",
       "NOVEMBER",
       "DECEMBER"
   };
   private StackPane numPane;
   

   @Override
   public void start(Stage primaryStage)
   {
      scene = new Scene(containerPane, 1000, 800);
      containerPane.setStyle("-fx-background-color: whitesmoke;");
      setupTopPane();
      GridPane gp = setupMonthPane(date.getYear(), date.getMonthValue());
      containerPane.setCenter(gp);
      
      primaryStage.setTitle("Calendar");
      primaryStage.setMinWidth(1000);
      primaryStage.setMinHeight(800);
      primaryStage.setScene(scene);
      primaryStage.show();
        
   }
   
   //Sets the navigation bar
   public void setupTopPane()
   {

       month.setText(date.getMonth() + "");
       month.setFont(Font.font(appointmentFile, FontWeight.THIN, FontPosture.REGULAR, 20));
       year.setText(date.getYear() + "");
       year.setFont(Font.font(appointmentFile, FontWeight.THIN, FontPosture.REGULAR, 20));
       monthAndYear.getChildren().addAll(month, year);
       monthAndYear.setAlignment(Pos.TOP_LEFT);
       monthAndYear.setPadding(navLeft);
       buttonNav.getChildren().addAll(leftBtn, yearBtn, todayBtn, rightBtn);
       buttonNav.setAlignment(Pos.TOP_RIGHT);
       buttonNav.setPadding(navRight);
       //navPane = new HBox(scene.getWidth() / 1.5);
       monthAndYear.prefWidthProperty().bind(scene.widthProperty().multiply(0.50));
        buttonNav.prefWidthProperty().bind(scene.widthProperty().multiply(0.50));
       navPane.setPadding(in);
       navPane.setAlignment(Pos.CENTER);
       navPane.getChildren().addAll(monthAndYear, buttonNav);
       containerPane.setTop(navPane);
       
   
   }
   
   //Pane to set up the month
   public GridPane setupMonthPane(int yearValue, int monthValue)
   {
      GridPane monthPane = new GridPane();     
      monthPane.setMaxWidth(scene.getWidth());
      monthPane.setMaxHeight(scene.getHeight()-150);
      containerPane.setPadding(new Insets(0, 10, 0, 10));
      
      for(int i = 0; i < 7; i++)
      {
          for(int j = 0; j < 7;j++)
          {
              sPane = new StackPane();
              sPane.setStyle("-fx-border-color: black;");
    
              if(i == 0)
              {
                  sPane.prefHeightProperty().bind(scene.heightProperty().divide(14));
                  sPane.prefWidthProperty().bind(scene.widthProperty().divide(7));
                  Text dayOfWeek = new Text(days[j]+"");
                  sPane.getChildren().add(dayOfWeek);
                  monthPane.add(sPane, j, i);
              
                  
              }
              else
              {
                  sPane.prefHeightProperty().bind(scene.heightProperty().divide(7));
                  sPane.prefWidthProperty().bind(scene.widthProperty().divide(7));
                  Text monthDays = new Text("");
                  sPane.getChildren().add(monthDays);
                  monthPane.add(sPane, j, i);
      
                  
              }
              
          }
        
      }     
      currentDay = date.getDayOfMonth();
      currentYear = LocalDate.of(yearValue, monthValue, currentDay).getYear();
      currentMonth = LocalDate.of(yearValue, monthValue, currentDay).getMonthValue();
      currentMonthName = LocalDate.of(yearValue, monthValue, currentDay).getMonth() + "";
      
        containerPane.setCenter(monthPane);
        monthPane.setStyle("-fx-border-color: black;");
      fillUpMonth(monthPane,yearValue,monthValue );
      return monthPane;
   }
   
   //Fills the calander 
   public void fillUpMonth(GridPane monthGP, int yearValue, int monthValue)
   {
       
       if(plug ==0)
      {
        month.setText(date.getMonth() + "");
        year.setText(date.getYear() + "");
      }
      else{
          month.setText(currentMonthName);
          year.setText(currentYear + "");
      }
    
       LocalDate firstDate = LocalDate.of(yearValue, monthValue, 1);
       LocalDate lastDate = LocalDate.of(yearValue, monthValue, 1).with(TemporalAdjusters.lastDayOfMonth());
       LocalDate lastMonth = lastDate.minusMonths(1);
       lastMonth = lastMonth.with(TemporalAdjusters.lastDayOfMonth());
       int lastDateOfLastMonth = lastMonth.getDayOfMonth();
       int lastDateOfMonth = lastDate.getDayOfMonth();
       String firstDayOfWeek = firstDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);       
       Circle red = new Circle();
       
       previousMonth = lastDate.getMonthValue();
       postMonth = currentMonth+1;
       
       int day = 1;
       int pos = 0;
       for(int x = 0; x < fullDays.length; x++)
       {
           if(fullDays[x].equals(firstDayOfWeek))
           {
               pos = x;
           }
       }
       int daysBeforeMonthStarts = (lastDateOfLastMonth - pos) +1;
      
       for(int i = 1; i < 7; i++)
       {
           for(int j = 0; j < 7; j++)
           {
               numPane = new StackPane();
               Text daysOfMonth = new Text("");
               //daysOfMonth.setFont(Font.font(appointmentFile, FontWeight.THIN, FontPosture.REGULAR, 20));
               
               if(i == 1 && j < pos)
               {
                   daysOfMonth.setText(daysBeforeMonthStarts + "");
                   daysOfMonth.setFill(Color.GREY);
                   //daysOfMonth.setFont(Font.font(appointmentFile, FontWeight.THIN, FontPosture.REGULAR, 20));
                   numPane.getChildren().add(daysOfMonth);
                   monthGP.add(numPane, j, i);
                   daysBeforeMonthStarts++;
               }
               else if( (i==6 || i==5)&& day < 13)
                {
                    daysOfMonth.setText(day+"");
                    daysOfMonth.setFill(Color.GREY);
                    numPane.getChildren().add(daysOfMonth);
                    monthGP.add(numPane, j, i);
                    day++;
                }
               else if(fullDays[j].equals(firstDayOfWeek) && i == 1)
                    {
                        day = 1;
                        daysOfMonth.setText(day+"");
                        if((currentDay == day) &&(date.getMonthValue() == currentMonth) &&(date.getYear() == currentYear))
                        {
                            red = new Circle(15, Color.RED);
                            numPane.getChildren().add(red);
                            daysOfMonth.setFill(Color.WHITE);
                            numPane.getChildren().add(daysOfMonth);
                            monthGP.add(numPane, j, i);
                        }
                        else{
                            daysOfMonth.setFill(Color.BLACK);
                            numPane.getChildren().add(daysOfMonth);
                            monthGP.add(numPane, j, i);
                        }
                        
                        day++;
                    }
               
               else if(day > lastDateOfMonth)
               {
                    day = 1;
                    daysOfMonth.setText(day+"");
                    daysOfMonth.setFill(Color.GREY);
                    numPane.getChildren().add(daysOfMonth);
                    monthGP.add(numPane, j, i);
                    day++;
                }
                else if(day < lastDateOfMonth)
                {
                    daysOfMonth.setText(day+"");
                    if((currentDay == day) &&(date.getMonthValue() == currentMonth) &&(date.getYear() == currentYear))
                        {
                            red = new Circle(15, Color.RED);                            
                            numPane.getChildren().add(red);
                            daysOfMonth.setFill(Color.WHITE);
                            numPane.getChildren().add(daysOfMonth);
                            monthGP.add(numPane, j, i);
                        }
                        else{
                            daysOfMonth.setFill(Color.BLACK);
                            numPane.getChildren().add(daysOfMonth);
                            monthGP.add(numPane, j, i);
                        }
                    day++;
                }
                
               else
                {
                    daysOfMonth.setText(day+"");
                    //daysOfMonth.setFill(Color.BLACK);
                    numPane.getChildren().add(daysOfMonth);
                    monthGP.add(numPane, j, i);
                    day++;
                }
                            
           }
       }
       String temp = "";
       
       leftBtn.setOnAction((ActionEvent e)->{
           if(currentMonth - 1 > 0){
               currentMonth = currentMonth -1;
           }
           else
           {
               currentYear--;
               currentMonth = 12;
           }
           plug = 1;
           monthGP.getChildren().clear();
           setupMonthPane(currentYear, currentMonth);
       });
       
       rightBtn.setOnAction((ActionEvent e)->{
           if(currentMonth + 1 > 12){
               currentMonth = 1;
               currentYear++;
           }
           else
           {
               currentMonth++;
               
           }
           plug = 1;
           monthGP.getChildren().clear();
           setupMonthPane(currentYear, currentMonth);
       });
       
       todayBtn.setOnAction((ActionEvent e)->{
           
           monthGP.getChildren().clear();
           setupMonthPane(date.getYear(), date.getMonthValue());
       });
       
       yearBtn.setOnAction((ActionEvent e)->{
           containerPane.setCenter(null);
           monthGP.getChildren().clear();
           twelveMonthsPane();
       });

   }
 
   //Creates the year view
   public GridPane twelveMonthsPane()
   {
      GridPane twelve = new GridPane();
      
      int mCounter = 0;
      int counter = 1;
       GridPane monthsGp = new GridPane();
       monthsGp.setHgap(10);
       monthsGp.setVgap(10);


        for(int bR = 0; bR < 3; bR++)
       {
           for(int bC = 0; bC < 4; bC++)
           {
               GridPane box = new GridPane();
               Text textMonth = new Text();
               textMonth.setText(months[mCounter]);
               box.getChildren().addAll(textMonth,setupMonthPane(currentYear, counter));
               VBox stack = new VBox(10);
               stack.setAlignment(Pos.CENTER);
               stack.setPadding(new Insets(10, 10, 10, 10));
               GridPane.setConstraints(stack, bC, bR);
               stack.getChildren().addAll(textMonth, box);              
               monthsGp.getChildren().add(stack);
               counter++;
               mCounter++;
           }
       }
       containerPane.setCenter(monthsGp);
       month.setText(" ");
       numPane.setStyle("-fx-border-color: whitesmoke");	
      return twelve;
   }
   
   
   
  
   //Clears the dates
   public void clear()
   {
      
      sPane.getChildren().clear();
      numPane.getChildren().clear();
      
      
   }

    
   
   public static void main(String[] args)
   {
      launch(args);
   }
}
