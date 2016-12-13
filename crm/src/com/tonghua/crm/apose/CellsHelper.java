package com.tonghua.crm.apose;

import com.aspose.cells.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by PP on 10/9/16.
 */
public class CellsHelper {
    static {
        try {
            new License().setLicense(CellsHelper.class.getClassLoader().getResourceAsStream("web/WEB-INF/apose/license.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Worksheet open(String excel, int sheetIndex) throws FileNotFoundException {
        if (excel == null || excel.length() == 0) {
            throw new FileNotFoundException("File not exist");
        }

        Workbook wb = null;
        try {
            LoadOptions opt = null;
            opt.setMemorySetting(MemorySetting.MEMORY_PREFERENCE);

            wb = new Workbook(excel, opt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        WorksheetCollection sheets = wb.getWorksheets();

        return sheets.get(sheetIndex);
    }

    public Worksheet open(String excel) throws FileNotFoundException {
        return open(excel, 0);
    }
}
