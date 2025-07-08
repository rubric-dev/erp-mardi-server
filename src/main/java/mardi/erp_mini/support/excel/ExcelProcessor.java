package mardi.erp_mini.support.excel;


import lombok.RequiredArgsConstructor;
import mardi.erp_mini.core.entity.user.UserRepository;
import mardi.erp_mini.exception.ExcelDownloadFailedException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExcelProcessor {
    private final ExcelManager excelManager;
    private final UserRepository userRepository;

    public ResponseEntity<Resource> downloadUserInvitationTemplate() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet creatorInvitationSheet = workbook.createSheet("유저 추가");

            creatorInvitationSheet.setColumnWidth(1, 15 * 256);

            int leftColumnOffset = 1;
            int headerMaxIndex = 0;
            List<String> headers = List.of("이메일");
            excelManager.setHeaders(creatorInvitationSheet.createRow(headerMaxIndex), headers, leftColumnOffset);

            int rowIndex = headerMaxIndex + 1;

            List<XSSFRow> rows = new ArrayList<>();
            while (rows.size() < 5) {
                XSSFRow row = creatorInvitationSheet.createRow(rowIndex++);
                rows.add(row);
            }

            return excelManager.convertToResponseEntity(workbook, "user_invitation_template");
        } catch (IOException e) {
            throw new ExcelDownloadFailedException();
        }
    }
}
