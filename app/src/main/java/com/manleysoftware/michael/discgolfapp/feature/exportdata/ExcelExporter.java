package com.manleysoftware.michael.discgolfapp.feature.exportdata;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.manleysoftware.michael.discgolfapp.data.Model.ScoreCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public static final int NAME_INDEX = 0;

    public static void exportScorecard(ScoreCard scoreCard, Context context) {

        File downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File fileOfExcelDoc = new File(downloadPath, "myExcelDoc.xls");
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

    private static void fillExcelDocFromScorecard(ScoreCard scoreCard, WritableSheet sheetA) throws WriteException {
        int columnCursor = 0;
        int rowCursor = 0;

        sheetA.addCell(new Label(columnCursor, rowCursor, "Course:"));
        sheetA.addCell(new Label(columnCursor + 1, rowCursor, scoreCard.getCourseName()));
        rowCursor++;

        sheetA.addCell(new Label(columnCursor, rowCursor, "Date:"));
        sheetA.addCell(new Label(columnCursor + 1, rowCursor, scoreCard.getDate()));
        rowCursor++;

        //print out Hole, 1, 2, .. , 18, Total header
        WritableCellFormat headerFormat = new WritableCellFormat();
        headerFormat.setBackground(Colour.LIGHT_GREEN);
        headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

        for (int i = 0; i < scoreCard.getCourse().getHoleCount() + 2; i++) {
            if (i == 0){
                WritableCell cell = new Label(i,rowCursor,"Hole");
                cell.setCellFormat(headerFormat);
                sheetA.addCell(cell);
                continue;
            }
            if (i == scoreCard.getCourse().getHoleCount() + 1){
                WritableCell cell = new Label(i, rowCursor, "Total");
                cell.setCellFormat(headerFormat);
                sheetA.addCell(cell);
                continue;
            }
            WritableCell cell = new Label(i, rowCursor, String.valueOf(i));
            cell.setCellFormat(headerFormat);
            sheetA.addCell(cell);
        }
        rowCursor++;


        WritableCellFormat parHeader = new WritableCellFormat();
        parHeader.setBackground(Colour.LIGHT_TURQUOISE);
        parHeader.setBorder(Border.ALL, BorderLineStyle.THIN);
        //print out Course pars
        for (int i = 0; i < scoreCard.getCourse().getHoleCount() + 2; i++) {
            if (i == 0){
                WritableCell cell = new Label(i, rowCursor, "Par");
                cell.setCellFormat(parHeader);
                sheetA.addCell(cell);
                continue;
            }
            if (i == scoreCard.getCourse().getHoleCount() + 1){
                WritableCell cell = new Label(i, rowCursor, String.valueOf(scoreCard.getCourse().getParTotal()));
                cell.setCellFormat(parHeader);
                sheetA.addCell(cell);
                continue;
            }
            WritableCell cell = new Label(i, rowCursor, String.valueOf(scoreCard.getCourse().getParArray()[i - 1]));
            cell.setCellFormat(parHeader);
            sheetA.addCell(cell);
        }
        rowCursor++;

        int PLAYER_TOTAL_INDEX = scoreCard.getCourse().getHoleCount() + 1;
        //Print out player scores
        for (int row = 0; row < scoreCard.getPlayersCount(); row++) {
            for (int columns = 0; columns < scoreCard.getCourse().getHoleCount() + 2; columns++) {
                if(columns == NAME_INDEX){
                    sheetA.addCell(new Label(columns,rowCursor, scoreCard.getPlayerArray()[row].getName()));
                } else if (columns == PLAYER_TOTAL_INDEX){
                    sheetA.addCell(new Label(columns,rowCursor, scoreCard.getPlayerArray()[row].getCurrentTotal()+""));
                } else{
                    sheetA.addCell(new Label(columns,rowCursor, scoreCard.getPlayerArray()[row].getScore()[columns-1]+""));
                }
            }
            rowCursor++;
        }
    }

    private static void notifyMediaScanner(Context context, File file){
        Intent intent =
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }
}
