package com.kropiejohn.ftc.glasses.controller;

import com.kropiejohn.ftc.glasses.model.*;
import javafx.collections.FXCollections;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

public class GlassesExcelParser {
    private final Map<String, BiConsumer<Cell, Glasses>> processorInputMap = new LinkedHashMap<>();
    private final Map<String, BiConsumer<Cell, Glasses>> processorOutputMap = new LinkedHashMap<>();

    public GlassesExcelParser() {
        // Initialize input map.
        processorInputMap.put("ID", (cell, glasses) -> glasses.setNumber((int) cell.getNumericCellValue()));

        processorInputMap.put("rightSphere", (cell, glasses) -> glasses.setRightSphere(cell.getNumericCellValue()));
        processorInputMap.put("rightCylinder", (cell, glasses) -> glasses.setRightCylinder(cell.getNumericCellValue()));
        processorInputMap.put("rightAxis", (cell, glasses) -> glasses.setRightAxis((int) cell.getNumericCellValue()));
        processorInputMap.put("rightFocal", (cell, glasses) -> glasses.setRightFocal((int) cell.getNumericCellValue()));

        processorInputMap.put("leftSphere", (cell, glasses) -> glasses.setLeftSphere(cell.getNumericCellValue()));
        processorInputMap.put("leftCylinder", (cell, glasses) -> glasses.setLeftCylinder(cell.getNumericCellValue()));
        processorInputMap.put("leftAxis", (cell, glasses) -> glasses.setLeftAxis((int) cell.getNumericCellValue()));
        processorInputMap.put("leftFocal", (cell, glasses) -> glasses.setLeftFocal((int) cell.getNumericCellValue()));

        processorInputMap.put("entryDate", (cell, glasses) -> glasses.setEntryDate(cell.getDateCellValue()));
        processorInputMap.put("removed", (cell, glasses) -> {
            if (cell.getCellType() == CellType.BOOLEAN) {
                glasses.setRemoved(cell.getBooleanCellValue());
            } else {
                glasses.setRemoved(YesNo.NO.getEnumFromAbbreviationOrName(cell.getStringCellValue()) == YesNo.YES);
            }
        });
        processorInputMap.put("sex", (cell, glasses) -> glasses.setGender(Gender.UNISEX.getEnumFromAbbreviationOrName(cell.getStringCellValue())));
        processorInputMap.put("age", (cell, glasses) -> glasses.setAge(Age.ADULT.getEnumFromAbbreviationOrName(cell.getStringCellValue())));
        processorInputMap.put("bifocals", (cell, glasses) -> glasses.setBifocals(YesNo.NO.getEnumFromAbbreviationOrName(cell.getStringCellValue())));


        // Initialize output map.
        processorOutputMap.put("ID", (cell, glasses) -> cell.setCellValue(glasses.getNumber()));

        processorOutputMap.put("rightSphere", (cell, glasses) -> cell.setCellValue(glasses.getRightSphere()));
        processorOutputMap.put("rightCylinder", (cell, glasses) -> cell.setCellValue(glasses.getRightCylinder()));
        processorOutputMap.put("rightAxis", (cell, glasses) -> cell.setCellValue(glasses.getRightAxis()));
        processorOutputMap.put("rightFocal", (cell, glasses) -> cell.setCellValue(glasses.getRightFocal()));

        processorOutputMap.put("leftSphere", (cell, glasses) -> cell.setCellValue(glasses.getLeftSphere()));
        processorOutputMap.put("leftCylinder", (cell, glasses) -> cell.setCellValue(glasses.getLeftCylinder()));
        processorOutputMap.put("leftAxis", (cell, glasses) -> cell.setCellValue(glasses.getLeftAxis()));
        processorOutputMap.put("leftFocal", (cell, glasses) -> cell.setCellValue(glasses.getLeftFocal()));

        processorOutputMap.put("entryDate", (cell, glasses) -> cell.setCellValue(glasses.getEntryDate()));
        processorOutputMap.put("removed", (cell, glasses) -> cell.setCellValue(glasses.isRemoved() ? YesNo.YES.getAbbreviation() : YesNo.NO.getAbbreviation()));
        processorOutputMap.put("sex", (cell, glasses) -> cell.setCellValue(glasses.getGender().getAbbreviation()));
        processorOutputMap.put("age", (cell, glasses) -> cell.setCellValue(glasses.getAge().getAbbreviation()));
        processorOutputMap.put("bifocals", (cell, glasses) -> cell.setCellValue(glasses.getBifocals().getAbbreviation()));
    }

    public void importDatabase(final Stage stage) throws IOException, InvalidFormatException {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Import database");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(FXCollections.observableArrayList(
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xlsm", "*.xls")));
        var fileToImport = fileChooser.showOpenDialog(stage);

        if (fileToImport == null) {
            return;
        }
        var workbook = new XSSFWorkbook(fileToImport);
        var worksheet = workbook.getSheetAt(0);

        var glassesList = new ArrayList<Glasses>();
        var headerMap = new HashMap<Integer, String>();
        worksheet.rowIterator().forEachRemaining(row -> {
            if (row.getRowNum() == 0) {
                headerMap.putAll(buildRowMap(row));
            } else {
                glassesList.add(processGlassesRow(row, headerMap));
            }
        });

        GlassesDatabase.INSTANCE.replaceContents(glassesList);
    }

    private Map<Integer, String> buildRowMap(final Row headerRow) {
        var map = new HashMap<Integer, String>();
        headerRow.cellIterator().forEachRemaining(cell -> map.put(cell.getColumnIndex(), cell.getStringCellValue()));
        return map;
    }

    private Glasses processGlassesRow(final Row glassesRow, final Map<Integer, String> headerData) {
        var glasses = new Glasses();
        glassesRow.forEach(cell -> processorInputMap.get(headerData.get(cell.getColumnIndex())).accept(cell, glasses));

        return glasses;
    }

    public void exportDatabase(final Stage stage) {
        var workBook = new XSSFWorkbook();
        var workSheet = workBook.createSheet("Glasses Database");
        var rowCounter = 0;
        var headerRow = workSheet.createRow(rowCounter++);

        // Build up header row.
        var columnCounter = 0;
        for (String columnName : processorOutputMap.keySet()) {
            headerRow.createCell(columnCounter++).setCellValue(columnName);
        }

        for (Glasses glasses : GlassesDatabase.INSTANCE.getAll()) {
            var newRow = workSheet.createRow(rowCounter++);
            columnCounter = 0;
            for (BiConsumer<Cell, Glasses> consumer : processorOutputMap.values()) {
                consumer.accept(newRow.createCell(columnCounter++), glasses);
            }
        }

        var fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Choose folder for export");
        var exportDirectory = fileChooser.showDialog(stage);

        if (exportDirectory == null) {
            return;
        }

        var month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        var year = Calendar.getInstance().get(Calendar.YEAR);
        var date = month + "_" + day + "_" + year;

        try (var fileOut = new FileOutputStream(exportDirectory.getPath() + "/ExportedDatabase_" + date + ".xlsx")) {
            workBook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
