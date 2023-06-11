package com.manleysoftware.michael.discgolfapp.feature.exportdata;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.manleysoftware.michael.discgolfapp.application.MutableInt;
import com.manleysoftware.michael.discgolfapp.domain.Scorecard;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExporter {


    public static void exportScorecard(Scorecard scoreCard, Context context) {

        File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String filename = createNewFileName();
        File fileOfExcelDoc = new File(downloadPath, filename);
        try {
            //file path
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale(Locale.ENGLISH.getLanguage(), Locale.ENGLISH.getCountry()));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(fileOfExcelDoc, wbSettings);

            //Excel sheetA first sheetA
            WritableSheet sheetA = workbook.createSheet("sheet A", 0);

            // column and row titles
            sheetA.addCell(new Label(0, 0, "sheet A 1"));
            fillExcelDocFromScorecard(scoreCard,sheetA);

            // close workbook
            workbook.write();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyMediaScanner(context, fileOfExcelDoc);

    }

    private static String createNewFileName() {
        String fileName = "Scorecard" + getCurrentDateTime() + ".xls";
        return fileName;
    }

    private static void fillExcelDocFromScorecard(Scorecard scoreCard, WritableSheet sheetA) throws WriteException {
        MutableInt columnCursor = new MutableInt(0);
        MutableInt rowCursor = new MutableInt(0);

        writeCourseInformation(scoreCard, sheetA, columnCursor, rowCursor);

        writeDateInformation(scoreCard, sheetA, columnCursor, rowCursor);

        //print out Hole, 1, 2, .. , 18, Total header
        writeHoleNumbersRow(scoreCard, sheetA, rowCursor);

        writeCourseParsRow(scoreCard, sheetA, rowCursor);

        writePlayerScores(scoreCard, sheetA, rowCursor);
    }

    private static void writePlayerScores(Scorecard scoreCard, WritableSheet sheetA, MutableInt rowCursor) throws WriteException {
        //Print out player scores
        for (int row = 0; row < scoreCard.getPlayersCount(); row++) {
            printPlayerScoreInfoOnRow(scoreCard, sheetA, rowCursor, row);
        }
    }

    private static void printPlayerScoreInfoOnRow(Scorecard scoreCard, WritableSheet sheetA, MutableInt rowCursor, int row) throws WriteException {
        int PLAYER_TOTAL_INDEX = scoreCard.getCourse().getHoleCount() + 1;
        int NAME_INDEX = 0;
        for (int columns = 0; columns < scoreCard.getCourse().getHoleCount() + 2; columns++) {
            if(columns == NAME_INDEX){
                sheetA.addCell(new Label(columns,rowCursor.getValue(), scoreCard.getPlayers().get(row).getName()));
            } else if (columns == PLAYER_TOTAL_INDEX){
                sheetA.addCell(new Label(columns,rowCursor.getValue(), scoreCard.getPlayers().get(row).getCurrentTotal()+""));
            } else{
                sheetA.addCell(new Label(columns,rowCursor.getValue(), scoreCard.getPlayers().get(row).getScores()[columns-1]+""));
            }
        }
        rowCursor.increment();
    }

    private static void writeCourseParsRow(Scorecard scoreCard, WritableSheet sheetA, MutableInt rowCursor) throws WriteException {
        WritableCellFormat parHeader = new WritableCellFormat();
        parHeader.setBackground(Colour.LIGHT_TURQUOISE);
        parHeader.setBorder(Border.ALL, BorderLineStyle.THIN);
        //print out Course pars
        for (int i = 0; i < scoreCard.getCourse().getHoleCount() + 2; i++) {
            if (i == 0){
                WritableCell cell = new Label(i, rowCursor.getValue(), "Par");
                cell.setCellFormat(parHeader);
                sheetA.addCell(cell);
                continue;
            }
            if (i == scoreCard.getCourse().getHoleCount() + 1){
                WritableCell cell = new Label(i, rowCursor.getValue(), String.valueOf(scoreCard.getCourse().getParTotal()));
                cell.setCellFormat(parHeader);
                sheetA.addCell(cell);
                continue;
            }
            WritableCell cell = new Label(i, rowCursor.getValue(), String.valueOf(scoreCard.getCourse().getParArray()[i - 1]));
            cell.setCellFormat(parHeader);
            sheetA.addCell(cell);
        }
        rowCursor.increment();
    }

    private static void writeHoleNumbersRow(Scorecard scoreCard, WritableSheet sheetA, MutableInt rowCursor) throws WriteException {
        WritableCellFormat headerFormat = new WritableCellFormat();
        headerFormat.setBackground(Colour.LIGHT_GREEN);
        headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

        for (int i = 0; i < scoreCard.getCourse().getHoleCount() + 2; i++) {
            if (i == 0){
                WritableCell cell = new Label(i,rowCursor.getValue(),"Hole");
                cell.setCellFormat(headerFormat);
                sheetA.addCell(cell);
                continue;
            }
            if (i == scoreCard.getCourse().getHoleCount() + 1){
                WritableCell cell = new Label(i, rowCursor.getValue(), "Total");
                cell.setCellFormat(headerFormat);
                sheetA.addCell(cell);
                continue;
            }
            WritableCell cell = new Label(i, rowCursor.getValue(), String.valueOf(i));
            cell.setCellFormat(headerFormat);
            sheetA.addCell(cell);
        }
        rowCursor.increment();
    }

    private static void writeDateInformation(Scorecard scoreCard, WritableSheet sheetA, MutableInt columnCursor, MutableInt rowCursor) throws WriteException {
        sheetA.addCell(new Label(columnCursor.getValue(), rowCursor.getValue(), "Date:"));
        sheetA.addCell(new Label(columnCursor.getValue() + 1, rowCursor.getValue(), scoreCard.displayDate()));
        rowCursor.increment();
    }

    private static void writeCourseInformation(Scorecard scoreCard, WritableSheet sheetA, MutableInt columnCursor, MutableInt rowCursor) throws WriteException {
        sheetA.addCell(new Label(columnCursor.getValue(), rowCursor.getValue(), "Course:"));
        sheetA.addCell(new Label(columnCursor.getValue() + 1, rowCursor.getValue(), scoreCard.getCourseName()));
        rowCursor.increment();
    }

    private static void notifyMediaScanner(Context context, File file){
        var intent =
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    private static String getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("_ddMMMyyyy_HHmmSSS");
        return dateFormat.format(calendar.getTime());
    }
}
