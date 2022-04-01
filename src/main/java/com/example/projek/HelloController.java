package com.example.projek;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class HelloController {

    @FXML
    private TextField tf_id;

    @FXML
    private TextField tf_name;

    @FXML
    private TextArea ta_address;

    @FXML
    private TextField tf_telp;

    @FXML
    private TableView<DataModel> employeeTable;

    @FXML
    private TableColumn<DataModel, String> idColumn;

    @FXML
    private TableColumn<DataModel, String> nameColumn;

    @FXML
    private TableColumn<DataModel, String> addressColumn;

    @FXML
    private TableColumn<DataModel, String> telpColumn;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private ObservableList<DataModel> employeeData;

    public void initialize(){
        final String JDBC_Driver = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost/data_karyawan";
        final String USERNAME = "root";
        final String PASSWORD = "";
        try{
            Class.forName(JDBC_Driver);
            connection = DriverManager.getConnection(DB_URL ,USERNAME, PASSWORD);
            statement = connection.createStatement();
            loadEmployee();
            getselectedTable();


        }catch (ClassNotFoundException e){
            System.out.println("Driver MySQL JDBC Failed!!! " + e);
        }catch (SQLException e2){
            System.out.println("ERROR " + e2);
        }
    }

    private void loadEmployee(){
        try {
            statement = connection.createStatement();

            String sql = "SELECT * FROM employee ORDER BY id ";
            resultSet = statement.executeQuery(sql);

            this.employeeData = FXCollections.observableArrayList();

            while (resultSet.next()){
                this.employeeData.add(new DataModel(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("telp")
                ));
            }
            view();
            resultSet.close();
            statement.close();


        }catch (SQLException e){
            System.out.println("Gagal Loading Data pegawai " + e);
        }
    }
    public void view(){
        this.idColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("id"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("name"));
        this.addressColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("address"));
        this.telpColumn.setCellValueFactory(new PropertyValueFactory<DataModel, String>("telp"));

        this.employeeTable.setItems(this.employeeData);
    }

    public void getselectedTable(){
        employeeTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DataModel dataModel = employeeTable.getItems().get(employeeTable.getSelectionModel().getSelectedIndex());
                tf_id.setText(dataModel.getId());
                tf_name.setText(dataModel.getName());
                ta_address.setText(dataModel.getAddress());
                tf_telp.setText(dataModel.getTelp());
            }
        });
    }

    public void cariId(){
        String id = tf_id.getText().trim();
        try{
            statement = connection.createStatement();
            String sql = "SELECT * FROM employee WHERE id = '"+id+"'";
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                //Line Terpenting pada tombol cari ID
                tf_id.setText(resultSet.getString("id"));
                tf_name.setText(resultSet.getString("name"));
                ta_address.setText(resultSet.getString("address"));
                tf_telp.setText(resultSet.getString("telp"));
            } else{
                myMessage(1, "ID Pegawai Tidak Ditemukan !!");
                tf_name.setText("");
                ta_address.setText("");
                tf_telp.setText("");
            }
        }catch (SQLException e){
            System.out.println("Gagal Load Data Dari DataBase " + e);
        }
    }

    public void simpanData(){
        String id = tf_id.getText().trim();
        String name = tf_name.getText().trim();
        String address = ta_address.getText().trim();
        String telp = tf_telp.getText().trim();

        if (id.equals("")||name.equals("")||address.equals("")||telp.equals("")){
            myMessage(1,"Data Harus Diisi !");
        }else {

            try {
                statement = connection.createStatement();
                String sqlSelect = "SELECT * FROM employee WHERE id ='"+id+"' ";
                resultSet = statement.executeQuery(sqlSelect);
                int record = 0;
                while (resultSet.next()){
                    record++;
                }

                if (record > 0){
                    myMessage(1, "ID Pegawai Sudah Ada");

                }else{
                    //memasukkan data ke database
                    String sqlInsert = "INSERT INTO employee VALUES('"+id+"','"+name+"','"+address+"','"+telp+"')";
                    int result = statement.executeUpdate(sqlInsert);

                    if (result > 0){
                        tf_id.setText("");
                        tf_name.setText("");
                        ta_address.setText("");
                        tf_telp.setText("");
                        myMessage(0,"Tambah Data Pegawai Sukses !");
                        loadEmployee();
                    }
                }
                resultSet.close();
                statement.close();
            }catch (SQLException e){
                System.out.println("Gagal Simpan Data !!! " + e);
            }
        }
    }

    public void ubahData(){
        String id = tf_id.getText().trim();
        String name = tf_name.getText();
        String address = ta_address.getText();
        String telp = tf_telp.getText();
        String idd = tf_id.getText().trim();


        try {
            statement = connection.createStatement();
            String sqlUpdate = "UPDATE employee SET id = '"+id+"', name = '"+name+"', address = '"+address+"', telp = '"+telp+"' WHERE id = '"+id+"' ";
            int r = statement.executeUpdate(sqlUpdate);

            String sql = "SELECT * FROM employee WHERE id = '"+idd+"'";
            resultSet = statement.executeQuery(sql);

            if (resultSet.next()){
                tf_id.setText("");
                tf_name.setText("");
                ta_address.setText("");
                tf_telp.setText("");
                myMessage(0, "Data Berhasil DiUpdate");
            }else {
                myMessage(1, "Id Tidak Ditemukan");
            }

            loadEmployee();
        }catch (SQLException e){

        }
    }

    public void hapusdata(){
        String id = tf_id.getText();
        try{
            statement = connection.createStatement();
            String sql="DELETE FROM employee WHERE id = '"+id+"'";
            int result = statement.executeUpdate(sql);

            if (result > 0){
                tf_id.setText("");
                tf_name.setText("");
                ta_address.setText("");
                tf_telp.setText("");
                myMessage(0, "Data Berhasil Dihapus !");
                loadEmployee();
            }
            statement.close();
        }catch (SQLException e){
            System.out.println("Gagal Hapus Data " + e);
        }
    }

    public void clearData(){
        tf_id.setText("");
        tf_name.setText("");
        ta_address.setText("");
        tf_telp.setText("");
    }

    private void myMessage(int type, String message){
        Alert alert = null;
        if (type == 0){
            alert = new Alert(Alert.AlertType.INFORMATION);
        }else if (type == 1){
            alert = new Alert(Alert.AlertType.WARNING);
        }
        alert.setTitle("Info !!!");
        alert.setContentText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}