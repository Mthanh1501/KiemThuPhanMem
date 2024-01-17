package com.supermarket.DAL;

import com.supermarket.DTO.Statistic;
import com.supermarket.utils.Date;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class StatisticDAL extends Manager {
    public StatisticDAL(){
        super("statistic",
            List.of("id",
                "date",
                "amount",
                "expenses",
                "deleted"
            ));
    }
    public List<Statistic> convertToStatistic(List<List<String>> data) {
        return convert(data, row -> {
            try {
                return new Statistic(
                    Integer.parseInt(row.get(0)), // id
                    Date.parseDate(row.get(1)), // date
                    Double.parseDouble(row.get(2)), // amount
                    Double.parseDouble(row.get(3)), // expenses
                    Boolean.parseBoolean(row.get(4)) //deleted
                );
            } catch (Exception e) {
                System.out.println("Error occurred in StatisticDAL.convertToStatistic(): " + e.getMessage());
            }
            return new Statistic();
        });
    }
    public int addStatistic(Statistic statistic) {
        try {
            return create(statistic.getId(),
                statistic.getDate(),
                statistic.getAmount(),
                statistic.getExpenses(),
                statistic.isDeleted()
            ); // discount khi tạo mặc định deleted = 0
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StatisticDAL.addStatistic(): " + e.getMessage());
        }
        return 0;
    }
    public int updateStatistic (Statistic statistic) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(statistic.getId());
            updateValues.add(statistic.getDate());
            updateValues.add(statistic.getAmount());
            updateValues.add(statistic.getExpenses());
            updateValues.add(statistic.isDeleted());
            return update(updateValues, "id = " + statistic.getId());
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StatisticDAL.updateStatistic(): " + e.getMessage());
        }
        return 0;
    }
    public int deleteStatistic(String... conditions) {
        try {
            List<Object> updateValues = new ArrayList<>();
            updateValues.add(true);
            return update(updateValues, conditions);
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StatisticDAL.deleteStatistic(): " + e.getMessage());
        }
        return 0;
    }
    public List<Statistic> searchStatistic(String... conditions) {
        try {
            return convertToStatistic(read(conditions));
        } catch (SQLException | IOException e) {
            System.out.println("Error occurred in StatisticDAL.searchStatistic(): " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
