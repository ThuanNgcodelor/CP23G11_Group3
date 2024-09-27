package thuan.dev.models.salary;

import thuan.dev.config.MyConnection;
import thuan.dev.controller.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SalaryImple implements SalaryDAO{
    Connection conn = MyConnection.getConnection();

    @Override
    public void countSalary2(Salary salary) {
        try{
            PreparedStatement statement = conn.prepareStatement("select sum(hours) as totalHours,sum(minutes) as totalMinutes,count(DISTINCT CAST(datetime as DATE)) as totalDays from loginTime where customerID = ? and status = 0");
            statement.setInt(1,salary.getCustomerID());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                long totalHours = rs.getLong("totalHours");
                long totalMinutes = rs.getLong("totalMinutes");
                long totalDays = rs.getLong("totalDays");

                totalHours += totalMinutes / 60 ;
                //So gio bang so phut chia 60
                totalMinutes = totalMinutes % 60;
                //So gio bang chia du

                System.out.println("Hours "+ totalHours + " Minutes "+totalMinutes + " Days " + totalDays);
                salary.setTotalHours(totalHours);
                salary.setTotalMinutes(totalMinutes);
                salary.setTotalDays(totalDays);


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void countSalary(Salary salary) {
        try{
            PreparedStatement statement = conn.prepareStatement("select sum(hours) as totalHours,sum(minutes) as totalMinutes,count(DISTINCT CAST(datetime as DATE)) as totalDays from loginTime where customerID = ? ");
            statement.setInt(1,Data.customerID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                long totalHours = rs.getLong("totalHours");
                long totalMinutes = rs.getLong("totalMinutes");
                long totalDays = rs.getLong("totalDays");

                totalHours += totalMinutes / 60 ;
                //So gio bang so phut chia 60
                totalMinutes = totalMinutes % 60;
                //So gio bang chia du

                System.out.println("Hours "+ totalHours + " Minutes "+totalMinutes + " Days " + totalDays);
                salary.setTotalHours(totalHours);
                salary.setTotalMinutes(totalMinutes);
                salary.setTotalDays(totalDays);


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void getSalary(Integer staffID) {
        LocalDateTime now = LocalDateTime.now();
        try {
            // Kiểm tra xem customerID có tồn tại hay không
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM salary WHERE customerID = ?");
            statement.setInt(1, staffID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int status = rs.getInt("status");
                if (status == 0) {
                    PreparedStatement updateStatement = conn.prepareStatement("UPDATE salary SET datetimeStart = ?,status = 1 WHERE customerID = ?");
                    updateStatement.setTimestamp(1, Timestamp.valueOf(now));
                    updateStatement.setInt(2, staffID);
                    updateStatement.executeUpdate();
                }
                //Bang 1 thì don't update
            }else {
                SalaryDAO salaryDAO = new SalaryImple();
                salaryDAO.timeStart(staffID);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Lấy thời gian hiện tại

    @Override
    public LocalDateTime timeStart(Integer staffID) {
        LocalDateTime now = LocalDateTime.now();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO salary(datetimeStart,hours,minutes,customerID,status) VALUES (?,?,?,?,?)");
            statement.setTimestamp(1, Timestamp.valueOf(now));
            statement.setInt(2,0);
            statement.setInt(3,0);
            statement.setInt(4,staffID);
            statement.setInt(5,1);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return now;
    }

    @Override
    public LocalDateTime timeEnd(Integer staffID) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeStart = null;

        try {
            // Lấy datetimeStart từ cơ sở dữ liệu
            PreparedStatement statement = conn.prepareStatement("SELECT datetimeStart,status FROM salary WHERE customerID = ?");
            statement.setInt(1,staffID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int status = rs.getInt("status");
                timeStart = rs.getTimestamp("datetimeStart").toLocalDateTime();
                if (status == 1){
                    // Cập nhật datetimeEnd trước khi tính toán
                    PreparedStatement statement1 = conn.prepareStatement("UPDATE salary SET datetimeEnd = ?,status = 0 WHERE customerID = ?");
                    statement1.setTimestamp(1, Timestamp.valueOf(now));
                    statement1.setInt(2,staffID);
                    statement1.executeUpdate();
                }
            }

            // Nếu datetimeStart khác null, tính khoảng thời gian giữa timeStart và timeEnd
            if (timeStart != null) {
                Duration duration = Duration.between(timeStart, now);

                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;

                // Cập nhật số giờ và phút vào cơ sở dữ liệu
                PreparedStatement updateStatement = conn.prepareStatement(
                        "UPDATE salary SET hours = ?, minutes = ? WHERE customerID = ?");
                updateStatement.setLong(1, hours);   // Cập nhật số giờ
                updateStatement.setLong(2, minutes); // Cập nhật số phút
                updateStatement.setInt(3,staffID);
                updateStatement.executeUpdate();

                PreparedStatement statement2 = conn.prepareStatement("INSERT INTO loginTime(hours,minutes,customerID,datetime,status) VALUES (?,?,?,?,?)");
                statement2.setLong(1,hours);
                statement2.setLong(2,minutes);
                statement2.setInt(3,staffID);
                statement2.setTimestamp(4,Timestamp.valueOf(now));
                statement2.setInt(4,0);
                statement2.executeUpdate();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return now;
    }

}
