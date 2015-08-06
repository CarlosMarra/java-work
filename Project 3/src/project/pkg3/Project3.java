package project.pkg3;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;          //importing all the things!!!

public class Project3 extends Application {
    
    static long word = 0;
    static long inst1 = 0;
    static long inst2 = 0;
    static long opcode1 = 0;
    static long opcode2 = 0;
    static long operand1 = 0;
    static long operand2 = 0;
    static int instruction = 0;
    static boolean cont = true;
    static boolean jump = false;     
    static boolean first = true;    //variable initializations

    static File file;               //object initializations
    static Machine machine = new Machine();
    static FileChooser fileChooser = new FileChooser();

    Text label;                     //labels!
    Button menuItem;                //buttons!
    TextField ac_Output; 
    TextField mq_Output;
    TextField pc_Output;
    TextField ir_Output;
    TextField result_Output;        //textfields 
    ListView<String> memoryView;    //essentially a text area

    @Override
    public void start(final Stage primaryStage) {

        GridPane grid = new GridPane();

        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(false);

        Scene scene = new Scene(grid, 800, 600);

        label = new Text("IAS Simulator v.0.0.0.1");
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        grid.add(label, 0, 0, 2, 1);

        label = new Text("Registers:");
        grid.add(label, 0, 3, 1, 1);

        label = new Text("AC:");
        grid.add(label, 0, 5, 1, 1);

        label = new Text("MQ:");
        grid.add(label, 0, 6, 1, 1);

        label = new Text("PC:");
        grid.add(label, 0, 7, 1, 1);

        label = new Text("IR:");
        grid.add(label, 0, 8, 1, 1);

        label = new Text("Instruction:");
        grid.add(label, 0, 10, 1, 1);

        label = new Text("Memory Values:");
        grid.add(label, 0, 12, 1, 1);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(1, 2, 1, 2));
        ac_Output = new TextField();
        hbox.getChildren().add(ac_Output);
        ac_Output.setPrefWidth(500);
        ac_Output.setEditable(false);
        grid.add(hbox, 2, 5);

        hbox = new HBox();
        hbox.setPadding(new Insets(1, 2, 1, 2));
        mq_Output = new TextField(); 
        hbox.getChildren().add(mq_Output);
        mq_Output.setPrefWidth(500);
        mq_Output.setEditable(false);
        grid.add(hbox, 2, 6);

        hbox = new HBox();
        hbox.setPadding(new Insets(1, 2, 1, 2));
        pc_Output = new TextField(); 
        hbox.getChildren().add(pc_Output);
        pc_Output.setPrefWidth(500);
        pc_Output.setEditable(false);
        grid.add(hbox, 2, 7);

        hbox = new HBox();
        hbox.setPadding(new Insets(1, 2, 1, 2));
        ir_Output = new TextField(); 
        hbox.getChildren().add(ir_Output);
        ir_Output.setPrefWidth(500);
        ir_Output.setEditable(false);
        grid.add(hbox, 2, 8);

        hbox = new HBox();
        hbox.setPadding(new Insets(1, 2, 1, 2));
        result_Output = new TextField(); 
        hbox.getChildren().add(result_Output);
        result_Output.setPrefWidth(500);
        result_Output.setEditable(false);
        grid.add(hbox, 2, 10);

        memoryView = new ListView<String>();
        VBox vbox = new VBox();
        vbox.setPrefHeight(200);
        vbox.setPrefWidth(600);
        vbox.setSpacing(5);
        vbox.getChildren().add(memoryView);
        grid.add(vbox, 2, 13, 2, 1);

        menuItem = new Button("Exit");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.exit();
            }
        });
        grid.add(menuItem, 0, 15, 6, 1);

        menuItem = new Button("Load");
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearView();
                machine = new Machine();
                fileChooser.setTitle("Open Resource File");
                file = fileChooser.showOpenDialog(primaryStage);
                try {
                    Scanner scanner = new Scanner(file);

                    while (scanner.hasNextLine()) {
                        word = Long.valueOf(scanner.nextLine().replace(" ", ""), 2);
                        machine.InitializeMemory(word);                     //all instructions initialized into memory
                    }
                    scanner.close();
                } catch (Exception e1) {
                    System.out.println("HALT: AN ERROR OCCURED!");
                    System.out.println(e1);
                }
            }
        });
        grid.add(menuItem, 2, 15, 6, 1);

        menuItem = new Button("Next");
        menuItem.setOnAction(new SteppingButtonHandler());
        grid.add(menuItem, 0, 16, 6, 1);

        menuItem = new Button("Run End");
        menuItem.setOnAction(new StepButtonHandler());
        grid.add(menuItem, 2, 16, 6, 1);

        StackPane root = new StackPane();

        primaryStage.setTitle("IASSimulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class StepButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            clearView();
            cont = true;

            while (cont) {

                if (jump) {
                    word = machine.MemoryAccess((long) machine.PC);
                    machine.PC++;
                    inst1 = (word & 0x000000FFFFF00000L) >> 20;     //If a jump occurs, the next instruction 
                    inst2 = (word & 0x00000000000FFFFFL);           //(dictated by the PC) occurs
                    opcode1 = inst1 >> 12;
                    operand1 = inst1 & 0xFFF;
                    opcode2 = inst2 >> 12;
                    operand2 = inst2 & 0xFFF;
                    jump = false;

                } else {

                    word = machine.MemoryAccess((long) machine.PC);
                    machine.PC++;

                    inst1 = (word & 0x000000FFFFF00000L) >> 20;     //The next two default instructions occur.
                    inst2 = (word & 0x00000000000FFFFFL);
                    opcode1 = inst1 >> 12;
                    operand1 = inst1 & 0xFFF;
                    opcode2 = inst2 >> 12;
                    operand2 = inst2 & 0xFFF;
                }
                instruction = 0;
                while (instruction < 2) {

                    filthy(opcode1, operand1);

                    if (instruction == 0) {            //essentially switching to the "right side" instruction within the word
                        instruction++;
                        inst1 = inst2;
                        operand1 = operand2;
                        opcode1 = opcode2;
                    } else if (instruction == 1) {     //ends the while loop, and goes to the next word
                        instruction = 2;
                    }

                    //if there are more instructions to be had, print out the result of the execution of the instruction
                    if (cont) {
                        updateView(machine.AC(), machine.MQ(), machine.MemDump(), machine.PC, machine.MemInit, opcode1, operand1);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    class SteppingButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            
            boolean halt = false;
            
            if (first && !halt) {

                if (jump) {
                    jump = false;
                    word = machine.MemoryAccess((long) machine.PC);
                    machine.PC++;

                    inst1 = (word & 0x000000FFFFF00000L) >> 20; //The first instruction. So 20B / 40B. Shifted by 20.
                    inst2 = (word & 0x00000000000FFFFFL);
                    opcode1 = inst1 >> 12;
                    operand1 = inst1 & 0xFFF;
                    opcode2 = inst2 >> 12;
                    operand2 = inst2 & 0xFFF;

                } else {
                    word = machine.MemoryAccess((long) machine.PC);
                    machine.PC++;

                    inst1 = (word & 0x000000FFFFF00000L) >> 20; //The first instruction. So 20B / 40B. Shifted by 20.
                    inst2 = (word & 0x00000000000FFFFFL);
                    opcode1 = inst1 >> 12;
                    operand1 = inst1 & 0xFFF;
                    opcode2 = inst2 >> 12;
                    operand2 = inst2 & 0xFFF;
                }

                filthy(opcode1, operand1);

                updateView(machine.AC(), machine.MQ(), machine.MemDump(), machine.PC, machine.MemInit, opcode1, operand1);
                first = false;
            } else if (!halt) {

                filthy(opcode2, operand2);

                updateView(machine.AC(), machine.MQ(), machine.MemDump(), machine.PC, machine.MemInit, opcode2, operand2);
                first = true;
            }
        }
    }

    public void clearView() {
        ac_Output.setText("");
        mq_Output.setText("");
        pc_Output.setText("");
        ir_Output.setText("");
        result_Output.setText("");
        memoryView.setItems(null);
        
        word = 0;
        inst1 = 0;
        inst2 = 0;
        opcode1 = 0;
        opcode2 = 0;
        operand1 = 0;
        operand2 = 0;
        instruction = 0;
        cont = true;
        jump = false;     
        first = true;    //variable initializations
    }

    public void updateView(long AC, long MQ, Long[] Memory, int PC, int MemInit, long IR, long operand1) {

        ac_Output.setText(String.format("%40s", Long.toBinaryString(AC)).replace(' ', '0') + " (" + AC + ")");
        mq_Output.setText(String.format("%40s", Long.toBinaryString(MQ)).replace(' ', '0') + " (" + MQ + ")");
        pc_Output.setText(String.format("%40s", Long.toBinaryString(PC)).replace(' ', '0') + " (" + PC + ")");
        ir_Output.setText(String.format("%40s", Long.toBinaryString(IR)).replace(' ', '0') + " (" + IR + ")");

        switch ((int) IR) {
            case 0b00001010:
                result_Output.setText("LOAD MQ: Transfer contents of register MQ to the AC.");
                break;
            case 0b00001001:
                result_Output.setText("LOAD MQ,MX: Transfer Contents of M(" + operand1 + ") to MQ");
                break;
            case 0b00100001:
                result_Output.setText("STOR M(X): Transfer Contents of accumulator to memory location " + operand1 + ".");
                break;
            case 0b00000001:
                result_Output.setText("LOAD M(X): Transfer M(" + operand1 + ") to the accumulator.");
                break;
            case 0b00000010:
                result_Output.setText("LOAD -M(X): Transfer -M(" + operand1 + ") to the accumulator.");
                break;
            case 0b00000011:
                result_Output.setText("LOAD |M(X)|: Transfer the absoulte value of M(" + operand1 + ") to the accumulator.");
                break;
            case 0b00000100:
                result_Output.setText("LOAD -|M(X)|: Transfer -|M(" + operand1 + ")| to the accumulator.");
                break;
            case 0b00001101:
                result_Output.setText("JUMP M(X,0:19): Take the next instruction from left half of M(" + operand1 + ").");
                break;
            case 0b00001110:
                result_Output.setText("JUMP M(X,20:39): Take the next instruction from right half of M(M(" + operand1 + ").");
                break;
            case 0b00001111:
                result_Output.setText("JUMP+M(X,0:19): If number in the accumulator is nonnegative, take the next instruction from left half of M(" + operand1 + ").");
                break;
            case 0b00010000:
                result_Output.setText("JUMP+M(X,20:39): If number in the accumulator is nonegative, take the next instruction from right half of M(" + operand1 + ").");
                break;
            case 0b00000101:
                result_Output.setText("ADD M(X): Add M(" + operand1 + ") to AC; put result in AC.");
                break;
            case 0b00000111:
                result_Output.setText("ADD |M(X)|: Add |M(" + operand1 + ")| to AC; put result in AC.");
                break;
            case 0b00000110:
                result_Output.setText("SUB M(X): Subtract M(" + operand1 + ") to AC; put result in AC.");
                break;
            case 0b00001000:
                result_Output.setText("SUB |M(X)|: Subtract |M(" + operand1 + ")| to AC; put result in AC.");
                break;
            case 0b00001011:
                result_Output.setText("MUL M(X): Multiply M(" + operand1 + ") by MQ; put most significant bits of result in AC, put least significant bits in MQ");
                break;
            case 0b00001100:
                result_Output.setText("DIV M(X): Divide AC by M(" + operand1 + "); put the quotient in MQ and remainder in AC.");
                break;
            case 0b00010100:
                result_Output.setText("LSH: Multiply accumulator by 2.");
                break;
            case 0b00010101:
                result_Output.setText("RSH: Divide accumulator by 2.");
                break;
            case 0b00010010:
                result_Output.setText("STOR M(X,8:19): Replace left address field at M(" + operand1 + ") by 12 rightmost bits of AC.");
                break;
            case 0b0010011:
                result_Output.setText("STOR M(X,28:39): Replace right address field at M(" + operand1 + ") by 12 rightmost bits of AC.");
                break;
            case 0b0000000:
                result_Output.setText("HALT");
                break;
        }
        ArrayList<String> memCellList = new ArrayList<String>();
        //in a loop
        for (int i = 0; i < MemInit; i++) {
            if (Memory[i] >= 0 && Memory[i] != null) {
                memCellList.add("At Address: " + i + "\t    " + String.format("%40s", Long.toBinaryString(Memory[i])).replace(' ', '0') + "(" + Memory[i] + ")");
                memoryView.setItems(FXCollections.observableArrayList(memCellList));
            } else {
                break;
            }
        }
    }

    public void filthy(long opcode, long operand) {

        switch ((int) opcode) {
            case 0b00001010:
                machine.MQAC();
                break;
            case 0b00001001:
                machine.LoadMQ(operand);
                break;
            case 0b00100001:
                machine.ACMemory(operand);
                break;
            case 0b00000001:
                machine.LoadAC(operand);
                break;
            case 0b00000010:
                machine.LoadAC(Math.negateExact(operand));
                break;
            case 0b00000011:
                machine.LoadAC(Math.abs(operand));
                break;
            case 0b00000100:
                machine.LoadAC(Math.negateExact(Math.abs(operand)));
                break;
            case 0b00001101:
                jump = true;
                machine.PC = (int) operand;
                break;
            case 0b00001110:
                jump = true;
                word = machine.MemoryAccess(operand);
                inst2 = (word & 0x00000000000FFFFFL);
                opcode2 = inst2 >> 12;
                operand2 = inst2 & 0xFFF;
                machine.PC = (int) operand;
                break;
            case 0b00001111:
                if (machine.AC() >= 0) {
                    jump = true;
                    word = machine.MemoryAccess(operand);
                    inst2 = (word & 0x000000FFFFF00000L) >> 20;
                    opcode2 = inst2 >> 12;
                    operand2 = inst2 & 0xFFF;
                    machine.PC = (int) operand;
                }
                break;
            case 0b00010000:
                if (machine.AC() >= 0) {
                    jump = true;
                    word = machine.MemoryAccess(operand);
                    inst2 = (word & 0x00000000000FFFFFL);
                    opcode2 = inst2 >> 12;
                    operand2 = inst2 & 0xFFF;
                    machine.PC = (int) operand;
                }
                break;
            case 0b00000101:
                machine.SetAC((machine.AC() + machine.MemoryAccess(operand)));
                break;
            case 0b00000111:
                machine.SetAC(machine.AC() + Math.abs(machine.MemoryAccess(operand)));
                break;
            case 0b00000110:
                machine.SetAC(machine.AC() - machine.MemoryAccess(operand));
                break;
            case 0b00001000:
                machine.SetAC(machine.AC() - Math.abs(machine.MemoryAccess(operand)));
                break;
            case 0b00001011:
                machine.SetAC(machine.MemoryAccess(operand) * machine.MQ());
                break;
            case 0b00001100:
                machine.SetAC(machine.AC() % machine.MemoryAccess(operand));
                machine.SetMQ(machine.AC() / machine.MemoryAccess(operand));
                break;
            case 0b00010100:
                machine.SetAC(machine.AC() * 2);
                break;
            case 0b00010101:
                machine.SetAC(machine.AC() / 2);
                break;
            case 0b00010010:
                break;
            case 0b0010011:
                break;
            case 0b0000000:
                cont = false;
                break;
        }
    }

    public static void main(String[] args) {
        launch(args); // initialization code (e.g. laod the program script)
    }
}