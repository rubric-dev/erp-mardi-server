package mardi.erp_mini.support.excel;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mardi.erp_mini.exception.ExcelDownloadFailedException;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelManager {

    // TODO: 추후 리팩터링

    @Nonnull
    public XSSFCellStyle createDefaultDataCellStyle(@Nonnull XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFDataFormat dataFormat = workbook.createDataFormat();

        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setDataFormat(dataFormat.getFormat("@"));

        return cellStyle;
    }

    @Nonnull
    public <T> XSSFCell setCellValueOrBlank(@Nonnull XSSFCell cell, @Nullable T value) {
        return setCellValueOrBlank(cell, value, null);
    }

    @Nonnull
    public <T> XSSFCell setCellValueOrBlank(@Nonnull XSSFCell cell, @Nullable T value, @Nullable CellStyle cellStyle) {
        if (value == null) {
            cell.setBlank();
        } else if (Boolean.class.isAssignableFrom(value.getClass())) {
            cell.setCellValue((Boolean) value);
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (RichTextString.class.isAssignableFrom(value.getClass())) {
            cell.setCellValue((RichTextString) value);
        } else {
            cell.setCellValue(value.toString());
        }

        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }

        return cell;
    }

    /**
     * 불러온 엑셀 iteration
     *
     * @param sheet          엑셀 시트
     * @param headerRowCount header row 숫자. 만약 비어있는 채로 row가 시작되면 해당 row를 skip하고 제공하기 때문에 수량 확인 필요!
     * @param formula        수행할 기능
     */
    public void iterExcel(Sheet sheet, int headerRowCount, Consumer<Row> formula) {
        Iterator<Row> iterator = sheet.rowIterator();
        for (int i = 0; i < headerRowCount; i++) {
            iterator.next(); // skip title
        }
        while (iterator.hasNext()) {
            Row row = iterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            if (cellIterator.hasNext()) {
                try {
                    formula.accept(row);
                } catch (NoSuchElementException ignored) {
                }
            }
        }
    }

    public static String stringCellValue(Row row, int cellIndex, DataFormatter formatter, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        String value = cell != null ? formatter.formatCellValue(cell, evaluator).trim() : null;
        return StringUtils.hasText(value) ? value : null;
    }

    public static Double doubleCellValue(Row row, int cellIndex) { // 현재 코드 그대로 사용하면 안되고 다른 메소드 처럼 조금 수정이 필요할 듯 함
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return cell != null ? cell.getNumericCellValue() : null;
    }

    public static Long longCellValue(Row row, int cellIndex, DataFormatter formatter, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        String value = cell != null ? formatter.formatCellValue(cell, evaluator).replace(",", "").trim() : null;
        return StringUtils.hasText(value) ? Long.valueOf(value) : null;
    }

    public static Integer intCellValue(Row row, int cellIndex, DataFormatter formatter, FormulaEvaluator evaluator) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        String value = cell != null ? formatter.formatCellValue(cell, evaluator).replace(",", "").trim() : null;
        return StringUtils.hasText(value) ? Integer.valueOf(value) : null;
    }

    // TODO: 추후 리팩터링
    public void setHeaders(XSSFRow headerRow, List<String> headers, int offset) {
        XSSFSheet sheet = headerRow.getSheet();
        XSSFWorkbook workbook = sheet.getWorkbook();

        CellStyle headerStyle = createHeaderStyleColorBlack(workbook);

        // 왼쪽 빈칸 추가
        for (int i = 0; i < offset; i++) {
            headerRow.createCell(i);
        }

        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i) == null) {
                continue;
            }
            XSSFCell cell = headerRow.createCell(i + offset);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(headers.get(i));
        }

        // 헤더에 필터 추가
        sheet.setAutoFilter(
                CellRangeAddress.valueOf(
                        getCellRangeAddressByHeaderIndex(
                                headerRow.getRowNum() + 1,
                                offset + 1,
                                offset + headers.size()
                        )
                )
        );
    }

    public void setHeadersWhite(XSSFRow headerRow, List<String> headers, int offset) {
        XSSFSheet sheet = headerRow.getSheet();
        XSSFWorkbook workbook = sheet.getWorkbook();

        CellStyle headerStyle = createHeaderStyleColorWhite(workbook);

        // 왼쪽 빈칸 추가
        for (int i = 0; i < offset; i++) {
            headerRow.createCell(i);
        }

        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i) == null) {
                continue;
            }
            XSSFCell cell = headerRow.createCell(i + offset);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(headers.get(i));
        }

        // 헤더에 필터 추가
        sheet.setAutoFilter(
                CellRangeAddress.valueOf(
                        getCellRangeAddressByHeaderIndex(
                                headerRow.getRowNum() + 1,
                                offset + 1,
                                offset + headers.size()
                        )
                )
        );
    }

    private CellStyle createHeaderStyleColorWhite(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createHeaderStyleColorBlack(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * 필터를 추가할 헤러 어드레스 입력 메서드
     */
    private static String getCellRangeAddressByHeaderIndex(int rowNum, int from, int to) {
        return String.format("%s%d:%s%d", getCharForNumber(from), rowNum, getCharForNumber(to), rowNum);
    }

    /**
     * 인덱스를 알파벳으로 변환 알파벳 범위에 속하지 않을때 A 고정
     */
    private static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : "A";
    }

    @Nonnull
    public ResponseEntity<Resource> convertToResponseEntity(
            @Nonnull XSSFWorkbook workbook,
            @Nonnull String filename
    ) {
        return convertToResponseEntity(null, null, workbook, filename);
    }

    @Nonnull
    public ResponseEntity<Resource> convertToResponseEntity(
            @Nullable HttpStatusCode httpStatusCode,
            @Nullable HttpHeaders httpHeaders,
            @Nonnull XSSFWorkbook workbook,
            @Nonnull String filename
    ) {
        HttpStatusCode httpStatus = httpStatusCode == null ? HttpStatus.OK : httpStatusCode;

        HttpHeaders headers = getExportBaseHeaders(filename);

        if (httpHeaders != null) {
            headers.addAll(httpHeaders); // 병합
        }

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(convertToStream(workbook));
    }


    @Nonnull
    public InputStreamResource convertToStream(@Nonnull XSSFWorkbook workbook) {
        XSSFFormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        formulaEvaluator.evaluateAll();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);

            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (IOException e) {
            throw new ExcelDownloadFailedException();
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    @Nonnull
    public HttpHeaders getExportBaseHeaders(@Nonnull String filename) {
        HttpHeaders httpHeaders = new HttpHeaders();

        String fileNameWithExt = filename + ".xlsx";
        String encodedFilename = URLEncoder.encode(fileNameWithExt, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        // ✅ filename (ASCII fallback) + filename* (UTF-8)
        String contentDisposition = "attachment; filename=\"" + fileNameWithExt + "\"; filename*=UTF-8''" + encodedFilename;

        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        httpHeaders.add(HttpHeaders.SET_COOKIE, "fileDownload=true; path=/");
        httpHeaders.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return httpHeaders;
    }


}
