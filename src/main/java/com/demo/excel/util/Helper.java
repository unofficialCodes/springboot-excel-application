package com.demo.excel.util;

import com.demo.excel.entity.Product;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Helper {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private Workbook w;
    private final List<Product> productList;

    public Helper(List<Product> productList) {
        this.productList = productList;
    }

    public void writeHeaderRow() {
        // create workbook and sheet.
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("products");

        // create a row in sheet
        XSSFRow row = sheet.createRow(0);

        // Fetch the fields
        Class<Product> productClass = Product.class;
        Field[] productFields = productClass.getDeclaredFields();

        // Iterate through the fields can create cell with values
        int cellIndex = 0;
        for (Field productField: productFields) {
            XSSFCell cell = row.createCell(cellIndex++);
            cell.setCellValue(productField.getName());
        }
    }
    public void writeDataRow() {
        int rowIndex = 1;

        for (Product product: productList) {
            XSSFRow row = sheet.createRow(rowIndex++);

            int cellIndex = 0;
            XSSFCell cell = row.createCell(cellIndex++);
            cell.setCellValue(product.getId().toString());

            cell = row.createCell(cellIndex++);
            cell.setCellValue(product.getName());

            cell = row.createCell(cellIndex++);
            cell.setCellValue(product.getDescription());
        }
    }
    public void export(HttpServletResponse servletResponse) {
        writeHeaderRow();
        writeDataRow();

        ServletOutputStream outputStream;
        try {
            outputStream = servletResponse.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static boolean validateExcel(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    public static List<Product> importData(InputStream inputStream) {
        List<Product> products = new ArrayList<>();

        XSSFWorkbook xssfWorkbook;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);
            XSSFSheet xssfSheet = xssfWorkbook.getSheet("products");
            Iterator<Row> rowIterator = xssfSheet.rowIterator();

            int rowIx = 0;
            while (rowIterator.hasNext()) {

                if (rowIx == 0) {
                    rowIx++;
                    rowIterator.next();
                    continue;
                }
                Row next = rowIterator.next();
                Iterator<Cell> cellIterator = next.iterator();

                int columnIx = 0;
                Product product = new Product();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    columnIx = nextCell.getColumnIndex();

                    switch (columnIx) {
                        case 0 -> {
                            product.setId(UUID.fromString(nextCell.getStringCellValue()));
                            System.out.println("Cell Address: ");
                            System.out.println(nextCell.getAddress());
                            System.out.println(nextCell.getAddress().toString());
                        }
                        case 1 -> product.setName(nextCell.getStringCellValue());
                        case 2 -> product.setDescription(nextCell.getStringCellValue());
                        default -> {
                        }
                    }
                }
                products.add(product);
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }
}
