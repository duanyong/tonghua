package com.tonghua.crm.service;

import com.aspose.cells.*;
import com.tonghua.crm.bean.*;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import me.duanyong.handswork.util.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by PP on 10/16/16.
 */
@org.springframework.stereotype.Service
public class ExcelService {
    private static final Logger log = LogManager.getLogger();

    @Autowired
    protected ApplicationContext context;

    public boolean syncToDatabase(String file) {

        LoadOptions opt = new LoadOptions();
        opt.setMemorySetting(MemorySetting.MEMORY_PREFERENCE);

        Workbook book = null;
        try {
            book = new Workbook(file, opt);
        } catch (Exception e) {
            return false;
        }

        //需要读取列表数据
        String  errmsg;
        String  nickname;
        String  staffno;
        String  departname;
        String  cardNo;
        String  checkinDate;
        String  checkinTime;
        String  deviceTitle;
        String  deviceNo;
        String  desc;
        Date datetime;

        User user;
        UserService userService = context.getBean(UserService.class);

        Department department;
        DepartmentService departmentService = context.getBean(DepartmentService.class);
        DepartmentStaffService staffService = context.getBean(DepartmentStaffService.class);

        TimeDevice timeDevice;
        TimeDeviceService timeDeviceService = context.getBean(TimeDeviceService.class);

        TimeCard timeCard;
        TimeCardService timeCardService = context.getBean(TimeCardService.class);

        TimeLog timeLog;
        TimeLogService timeLogService = context.getBean(TimeLogService.class);

        Cells cells = book.getWorksheets().get(0).getCells();


        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SimpleDateFormat format;
        format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getDefault());

        int maxRows     = cells.getMaxDataRow() + 1;
        for (int i = 1; i < maxRows; ++ i) {
            departname  = cells.get(i, 0).getStringValue();
            staffno     = cells.get(i, 1).getStringValue();
            nickname    = cells.get(i, 2).getStringValue();
            cardNo      = cells.get(i, 3).getStringValue();
            deviceNo    = cells.get(i, 4).getStringValue();
            deviceTitle = cells.get(i, 5).getStringValue();
            checkinDate = cells.get(i, 6).getStringValue();
            checkinTime = cells.get(i, 7).getStringValue();
            desc        = cells.get(i, 8).getStringValue();
            datetime    = DateTimeUtil.getDate(checkinDate + " " + checkinTime);


            errmsg      = null;

            //获取员工编号，取得用户对应的卡号
            if (( user = userService.findByStaffno(staffno) ) == null) {

                //考勤机上没有对应此用户，需要重新创建一个新用户
                user = userService.importFromExcel(nickname, staffno, datetime);
                if (user == null) {
                    //新增用户失败
                    user = new User();

                    errmsg = "新增用户失败";
                }
            }

            //获取部门信息
            if (( department = departmentService.find(departname) ) == null) {
                //没有部门信息，需要重新创建一个新用户
                department = departmentService.importFromExcel(departname);

                if (department == null) {
                    department = new Department();

                    errmsg = "新增部门信息失败";
                }
            }

            //添加部门与人员之间的关系
            if (staffService.findStaff(department.getId(), user.getId()) == null) {
                if (staffService.importFromExcel(department.getId(), user.getId()) == null) {
                    errmsg = "新增部门与人员关系失败";
                }
            }


            //获取门禁设备信息
            if (( timeDevice = timeDeviceService.find(deviceNo) ) == null) {
                //没有部门信息，需要重新创建一个新用户
                timeDevice = timeDeviceService.importFromExcel(deviceNo, deviceTitle);

                if (timeDevice == null) {
                    timeDevice = new TimeDevice();

                    errmsg = "新增门禁信息失败";
                }
            }

            //获取门禁门卡信息

            if (( timeCard = timeCardService.findByUserAndCard(user.getId(), cardNo) ) == null) {
                //没有门卡信息
                timeCard = timeCardService.importFromExcel(user.getId(), cardNo);
                log.info("创建门卡信息：" + user.getId() + "卡号：" + cardNo);

                if (timeCard == null) {
                    timeCard = new TimeCard();

                    errmsg = "新增门卡信息失败";
                }
            }

            //获取门禁记录信息
            if (( timeLog = timeLogService.findByUserAndTime(user.getId(), datetime, datetime) ) == null) {
                //没有部门信息，需要重新创建一个新用户
                if (errmsg != null) {
                    //之前导入的信息失败
                    desc += " ->" + errmsg;
                }

                timeLog = timeLogService.importFromExcel(user.getId(), timeDevice.getId(), timeCard.getId(), datetime, datetime, desc);

                if (timeLog == null) {
                    log.info("导入数据失败：" + desc);
                } else {
                    log.info(i + "-" + nickname + "-" + staffno + "-" + checkinDate + " " + checkinTime);
                }
            }
        }

        return true;
    }
}


