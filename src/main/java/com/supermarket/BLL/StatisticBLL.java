package com.supermarket.BLL;

import com.supermarket.DAL.StatisticDAL;
import com.supermarket.DTO.Statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticBLL extends Manager<Statistic> {
    private StatisticDAL statisticDAL;
    private List<Statistic> statisticList;
    public StatisticBLL(){
        statisticDAL = new StatisticDAL();
        statisticList = searchStatistic("deleted = 0");
    }
    public StatisticDAL getStatisticDAL() {
        return statisticDAL;
    }

    public void setStatisticDAL(StatisticDAL statisticDAL) {
        this.statisticDAL = statisticDAL;
    }

    public List<Statistic> getStatisticList() {
        return statisticList;
    }
    public void setStatisticList(List<Statistic> statisticList) {
        this.statisticList = statisticList;
    }

    public Object[][] getData() {
        return getData(statisticList);
    }

    public boolean addStatistic(Statistic statistic) {
        statisticList.add(statistic);
        return statisticDAL.addStatistic(statistic) != 0;
    }

    public boolean updateStatistic(Statistic statistic) {
        statisticList.set(getIndex(statistic, "id", statisticList), statistic);
        return statisticDAL.updateStatistic(statistic) != 0;
    }

    public boolean deleteStatistic(Statistic statistic) {
        statisticList.remove(getIndex(statistic, "id", statisticList));
        return statisticDAL.deleteStatistic("id = " + statistic.getId()) != 0;
    }

    public List<Statistic> searchStatistic(String... conditions) {
        return statisticDAL.searchStatistic(conditions);
    }
    public List<Statistic> findStatistics(String key, String value) {
        List<Statistic> list = new ArrayList<>();
        for (Statistic statistic : statisticList) {
            if (getValueByKey(statistic, key).toString().toLowerCase().contains(value.toLowerCase())) {
                list.add(statistic);
            }
        }
        return list;
    }
    public List<Statistic> findStatisticsBy(Map<String, Object> conditions) {
        List<Statistic> statistics = statisticList;
        for (Map.Entry<String, Object> entry : conditions.entrySet())
            statistics = findObjectsBy(entry.getKey(), entry.getValue(), statistics);
        return statistics;
    }
    public boolean exists(Statistic statistic) {
        return !findStatisticsBy(Map.of(
            "id", statistic.getId(),
            "date", statistic.getDate(),
            "amount", statistic.getAmount(),
            "expenses",statistic.getExpenses(),
            "deleted",statistic.isDeleted()
        )).isEmpty();
    }
    public boolean exists(Map<String, Object> conditions) {
        return !findStatisticsBy(conditions).isEmpty();
    }
    @Override
    public Object getValueByKey(Statistic statistic, String key) {
        return switch (key) {
            case "id" -> statistic.getId();
            case "date" -> statistic.getDate();
            case "amount" -> statistic.getAmount();
            case "expenses" -> statistic.getExpenses();
            case "deleted" -> statistic.isDeleted();
            default -> null;
        };
    }
}
