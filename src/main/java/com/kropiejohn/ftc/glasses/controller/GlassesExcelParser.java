package com.kropiejohn.ftc.glasses.controller;

import com.kropiejohn.ftc.glasses.model.*;
import javafx.collections.FXCollections;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class GlassesExcelParser {
    private final Map<String, BiConsumer<Cell, Glasses>> processorInputMap = new HashMap<>();
    private final Map<String, BiConsumer<Cell, Glasses>> processorOutputMap = new HashMap<>();

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
        processorOutputMap.put("ID", (cell, glasses) -> glasses.setNumber((int) cell.getNumericCellValue()));

        processorOutputMap.put("rightSphere", (cell, glasses) -> glasses.setRightSphere(cell.getNumericCellValue()));
        processorOutputMap.put("rightCylinder", (cell, glasses) -> glasses.setRightCylinder(cell.getNumericCellValue()));
        processorOutputMap.put("rightAxis", (cell, glasses) -> glasses.setRightAxis((int) cell.getNumericCellValue()));
        processorOutputMap.put("rightFocal", (cell, glasses) -> glasses.setRightFocal((int) cell.getNumericCellValue()));

        processorOutputMap.put("leftSphere", (cell, glasses) -> glasses.setLeftSphere(cell.getNumericCellValue()));
        processorOutputMap.put("leftCylinder", (cell, glasses) -> glasses.setLeftCylinder(cell.getNumericCellValue()));
        processorOutputMap.put("leftAxis", (cell, glasses) -> glasses.setLeftAxis((int) cell.getNumericCellValue()));
        processorOutputMap.put("leftFocal", (cell, glasses) -> glasses.setLeftFocal((int) cell.getNumericCellValue()));

        processorOutputMap.put("entryDate", (cell, glasses) -> glasses.setEntryDate(cell.getDateCellValue()));
        processorOutputMap.put("removed", (cell, glasses) -> {
            if (cell.getCellType() == CellType.BOOLEAN) {
                glasses.setRemoved(cell.getBooleanCellValue());
            } else {
                glasses.setRemoved(YesNo.NO.getEnumFromAbbreviationOrName(cell.getStringCellValue()) == YesNo.YES);
            }
        });
        processorOutputMap.put("sex", (cell, glasses) -> glasses.setGender(Gender.UNISEX.getEnumFromAbbreviationOrName(cell.getStringCellValue())));
        processorOutputMap.put("age", (cell, glasses) -> glasses.setAge(Age.ADULT.getEnumFromAbbreviationOrName(cell.getStringCellValue())));
        processorOutputMap.put("bifocals", (cell, glasses) -> glasses.setBifocals(YesNo.NO.getEnumFromAbbreviationOrName(cell.getStringCellValue())));


    }

    public void importDatabase(final Stage stage) throws IOException, InvalidFormatException {
        var fileChooser = new FileChooser();
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
        for (String columnName : processorInputMap.keySet()) {
            headerRow.createCell(columnCounter++).setCellValue(columnName);
        }

        var glassesValues = GlassesDatabase.INSTANCE.get().values();
        for (Glasses glasses : glassesValues) {
            var newRow = workSheet.createRow(rowCounter++);

        }
    }
}
