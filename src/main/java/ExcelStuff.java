import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelStuff {
    static void writeToExcel(List<Post> posts){
        Workbook workbook = new XSSFWorkbook();
        Sheet dataSheet = workbook.createSheet("data");
        Row header = dataSheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Author");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Timestamp");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Body");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Reply From Author");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Engagement");
        headerCell.setCellStyle(headerStyle);

        dataSheet.setColumnWidth(0, 30 * 256);
        dataSheet.setColumnWidth(1, 30 * 256);
        dataSheet.setColumnWidth(2, 30 * 256);
        dataSheet.setColumnWidth(3, 30 * 256);
        dataSheet.setColumnWidth(4, 30 * 256);

        //CONTENT STUFF

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        for (int i = 0; i < posts.size(); i++) {
            Post current = posts.get(i);

            Row row = dataSheet.createRow(i+1);


            //Author
            Cell cell = row.createCell(0);
            cell.setCellValue(current.getAuthor());
            cell.setCellStyle(style);

            //Timestamp
            Cell cellTime = row.createCell(1);
            cellTime.setCellValue(current.getTimestamp());
            cellTime.setCellStyle(style);

            //Body
            Cell cellBody = row.createCell(2);
            cellBody.setCellValue(current.getBody());
            cellBody.setCellStyle(style);

            //ReplyFromAuthor
            Cell cellReply = row.createCell(3);
            cellReply.setCellValue(current.getReplyFromAuthor());
            cellReply.setCellStyle(style);

            //Engagement
            Cell cellEngagement = row.createCell(4);
            cellEngagement.setCellValue(current.getEngagement());
            cellEngagement.setCellStyle(style);

        }


        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
