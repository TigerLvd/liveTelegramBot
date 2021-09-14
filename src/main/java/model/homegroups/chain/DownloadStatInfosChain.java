package model.homegroups.chain;

import model.homegroups.db.HomeGroup;
import model.homegroups.db.StatInfo;
import model.homegroups.db.User;
import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import utils.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class DownloadStatInfosChain extends Chain {
    private final Pattern pattern = Pattern.compile("\\s*Скачать\\s\\s*статистику\\s\\s*в\\s\\s*xsl\\s*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    @Override
    public boolean check(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        return pattern.matcher(message.getText()).matches();
    }

    @Override
    public void doJob(DBFacade dbFacade, BotFacade botFacade, Message message, CallbackQuery callbackQuery, Map<String, Object> atr) {
        downloadStatInfos(dbFacade, botFacade, message.getChatId(), buildKeyboardForUser((User) atr.get(USER_FIELD)));
    }

    public void downloadStatInfos(DBFacade dbFacade, BotFacade botFacade, Long chatId, ReplyKeyboardMarkup keyboardMarkup) {
        botFacade.sendMsg(chatId, "Сбор информации, формирование файла...", keyboardMarkup);
        try {
            List<HomeGroup> homeGroups = dbFacade.getHomeGroupService().findAll();
            Map<Date, Map<HomeGroup, StatInfo>> map = new HashMap<>();
            if (homeGroups != null && !homeGroups.isEmpty()) {
                for (HomeGroup homeGroup : homeGroups) {
                    Date day = Utils.getStartYearDate();
                    Map<Date, StatInfo> statInfoByFirstDayOfWeek = getMapStatInfoByFirstDayOfWeek(dbFacade, homeGroup.getId());
                    while (day.before(new Date())) {
                        map.computeIfAbsent(day, k -> new HashMap<>());
                        map.get(day).put(homeGroup, statInfoByFirstDayOfWeek.getOrDefault(day, null));
                        day = Utils.addDay(day, 7);
                    }
                }
            }
            String path = "statInfo.xsl";
            writeIntoExcel(path, map);
            botFacade.sendFile(chatId, path, keyboardMarkup);
        } catch (IOException e) {
            botFacade.sendMsg(chatId, "Системная ошибка", keyboardMarkup);
            e.printStackTrace();
        }
    }

    private Map<Date, StatInfo> getMapStatInfoByFirstDayOfWeek(DBFacade dbFacade, Long homeGroupId) {
        Map<Date, StatInfo> statInfoByFirstDayOfWeek = new HashMap<>();
        List<StatInfo> allStatInfos = dbFacade.getStatInfoService().findAllByHomeGroupId(homeGroupId);
        if (null != allStatInfos) {
            for (StatInfo statInfo : allStatInfos) {
                Date time = Utils.getFirstDayOfWeek(statInfo.getEventDate());
                statInfoByFirstDayOfWeek.put(time, statInfo);
            }
        }
        return statInfoByFirstDayOfWeek;
    }

    @SuppressWarnings("deprecation")
    private void writeIntoExcel(String path, Map<Date, Map<HomeGroup, StatInfo>> map) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Статистика посещений ячеек");

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Яча\\Дата");

        Iterator<Date> iterator = map.keySet().iterator();
        Date firstDate = iterator.next();
        for (HomeGroup homeGroup : map.get(firstDate).keySet()) {
            Cell name = headerRow.createCell(homeGroup.getId().intValue());
            name.setCellValue(homeGroup.getComment());
        }
        // Меняем размер столбца
        sheet.autoSizeColumn(0);

        int rowNumb = 1;
        for (Date date : map.keySet()) {
            Row row = sheet.createRow(rowNumb);

            Cell dateCell = row.createCell(0);
            DataFormat format = book.createDataFormat();
            CellStyle dateStyle = book.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            dateCell.setCellStyle(dateStyle);
            dateCell.setCellValue(date);

            for (HomeGroup homeGroup : map.get(date).keySet()) {
                Cell stCell = row.createCell(homeGroup.getId().intValue());
                StatInfo statInfo = map.get(date).get(homeGroup);
                if (null != statInfo) {
                    stCell.setCellValue(statInfo.getCount());
                } else {
                    stCell.setCellValue(0);
                }
            }
            // Меняем размер столбца
            sheet.autoSizeColumn(rowNumb);

            rowNumb++;
        }

        // Записываем всё в файл
        book.write(new FileOutputStream(path));
        book.close();
    }
}
