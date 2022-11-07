package com.example.excelreader.sabteAsnad.paya.service;

import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.sabteAsnad.paya.dao.*;
import com.example.excelreader.sabteAsnad.paya.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportPayaTablesService {
    IncomingRegularTransactionDao incomingRegularTransactionDao;
    IncomingReturnedTransactionDao incomingReturnedTransactionDao;
    OutgoingTransactionDao outgoingTransactionDao;
    RejectedTransactionDao rejectedTransactionDao;
    ReturnedRegularTransactionDao returnedRegularTransactionDao;
    ReturnedShaparakTransactionDao returnedShaparakTransactionDao;
    Integer counter;
    List<String> circleList;

    public ImportPayaTablesService(IncomingRegularTransactionDao incomingRegularTransactionDao,
                                   IncomingReturnedTransactionDao incomingReturnedTransactionDao,
                                   OutgoingTransactionDao outgoingTransactionDao,
                                   RejectedTransactionDao rejectedTransactionDao,
                                   ReturnedRegularTransactionDao returnedRegularTransactionDao,
                                   ReturnedShaparakTransactionDao returnedShaparakTransactionDao) {
        this.incomingRegularTransactionDao = incomingRegularTransactionDao;
        this.incomingReturnedTransactionDao = incomingReturnedTransactionDao;
        this.outgoingTransactionDao = outgoingTransactionDao;
        this.rejectedTransactionDao = rejectedTransactionDao;
        this.returnedRegularTransactionDao = returnedRegularTransactionDao;
        this.returnedShaparakTransactionDao = returnedShaparakTransactionDao;
        this.counter = 0;
        this.circleList = new ArrayList<>();
    }


    public void breakIncomingRegularTransaction(MultipartFile multipartFile) {
        List<String> list = Helper.getAsList(multipartFile);
        this.counter = 0;
        this.circleList = breakToLists(list,circleList,"چرخه دوم");
        System.out.println(circleList);
        System.out.println(this.counter);
        saveIncomingRegularTransaction(this.circleList, 1);
        this.circleList = breakToLists(list,circleList,"چرخه سوم");
        System.out.println(circleList);
        System.out.println(this.counter);
        saveIncomingRegularTransaction(this.circleList, 2);
        this.circleList = breakToLists(list,circleList,"چرخه چهارم");
        System.out.println(circleList);
        System.out.println(this.counter);
        saveIncomingRegularTransaction(this.circleList, 3);
        this.circleList = breakToLists(list,circleList," ");
        System.out.println(circleList);
        System.out.println(this.counter);
        saveIncomingRegularTransaction(this.circleList, 4);
    }

    public void saveIncomingRegularTransaction(List<String> list,Integer circleNo) {
        for (int i = 0; i < list.size(); i = i + 12) {
            IncomingRegularTransaction irt = new IncomingRegularTransaction();
            irt.setTransactionId(list.get(i));
            irt.setTrackingNumber(list.get(i + 1));
            irt.setAmount(Long.parseLong(list.get(i + 2)));
            irt.setSenderFullName(list.get(i + 3));
            irt.setSenderId(list.get(i + 4));
            irt.setDebitPartyAccountNumber(list.get(i + 5));
            irt.setRemittingBank(list.get(i + 6));
            irt.setReceiverFullName(list.get(i + 7));
            irt.setCreditPartyAccountNumber(list.get(i + 8));
            irt.setDepositId(list.get(i + 9));
            irt.setTransactionDescription(list.get(i + 10));
            irt.setRemittingBankBranchCode(list.get(i + 11));
            irt.setCircle(circleNo);
            System.out.println(irt);
            incomingRegularTransactionDao.save(irt);
        }
    }

    public void saveIncomingReturnedTransaction (MultipartFile multipartFile) {
        List<String> list = Helper.getAsList(multipartFile);
        for (int i = 0; i < list.size(); i = i + 8) {
            IncomingReturnedTransaction irt = new IncomingReturnedTransaction();
            irt.setReturnedId(list.get(i));
            irt.setPrimitiveTrackingNumber(list.get(i + 1));
            irt.setPrimitiveTransactionNumber(list.get(i + 2));
            irt.setPrimitiveAmount(Long.parseLong(list.get(i + 3)));
            irt.setReturnedAmount(Long.parseLong(list.get(i + 4)));
            irt.setReturnReason(list.get(i + 5));
            irt.setReferenceAccount(list.get(i + 6));
            irt.setRemittingBankBranchCode(list.get(i + 7));
            incomingReturnedTransactionDao.save(irt);
        }
    }

    public void breakOutgoingTransaction (MultipartFile multipartFile) {
        List<String> list = Helper.getAsList(multipartFile);
        this.counter = 0;
        this.circleList = breakToLists(list,circleList,"چرخه دوم");
        saveOutgoingTransaction(this.circleList,1);
        this.circleList = breakToLists(list,circleList,"چرخه سوم");
        saveOutgoingTransaction(this.circleList,2);
        this.circleList = breakToLists(list,circleList,"چرخه چهارم");
        saveOutgoingTransaction(this.circleList,3);
        this.circleList = breakToLists(list,circleList," ");
        saveOutgoingTransaction(this.circleList,4);
    }

    public void saveOutgoingTransaction (List<String> list, Integer circleNo) {

        for (int i = 0; i < list.size(); i = i + 14) {
            OutgoingTransaction ot = new OutgoingTransaction();
            ot.setTransactionId(list.get(i));
            ot.setTrackingNumber(list.get(i + 1));
            ot.setAmount(Long.parseLong(list.get(i + 2)));
            ot.setSenderFullName(list.get(i + 3));
            ot.setSenderId(list.get(i + 4));
            ot.setDebitPartyAccountNumber(list.get(i + 5));
            ot.setReceivingBank(list.get(i + 6));
            ot.setReceiverFullName(list.get(i + 7));
            ot.setCreditPartyAccountNumber(list.get(i + 8));
            ot.setDepositId(list.get(i + 9));
            ot.setTransactionDescription(list.get(i + 10));
            ot.setBankBranchCode(list.get(i + 11));
            ot.setPaymentSenderBankBranchCode(list.get(i + 12));
            ot.setTransactionClientId(list.get(i + 13));
            ot.setStamp(list.get(i + 14));
            ot.setCircle(circleNo);
            outgoingTransactionDao.save(ot);
        }
    }

    public void saveRejectedTransaction (MultipartFile multipartFile) {
        List<String> list = Helper.getAsList(multipartFile);
        for (int i = 0; i < list.size(); i = i + 8) {
            RejectedTransaction rt = new RejectedTransaction();
            rt.setTransactionId(list.get(i));
            rt.setTrackingNumber(list.get(i + 1));
            rt.setAmount(Long.parseLong(list.get(i + 2)));
            rt.setSenderFullName(list.get(i + 3));
            rt.setSenderId(list.get(i + 4));
            rt.setSenderAccountNumber(list.get(i + 5));
            rt.setReceivingBank(list.get(i + 6));
            rt.setReceiverFullName(list.get(i + 7));
            rt.setReceiverFullName(list.get(i + 8));
            rt.setReceiverFullName(list.get(i + 9));
            rt.setReceiverFullName(list.get(i + 10));
            rt.setReceiverFullName(list.get(i + 11));
            rt.setReceiverFullName(list.get(i + 12));
            rejectedTransactionDao.save(rt);
        }

    }

    public void breakReturnRegularTransaction (MultipartFile multipartFile) {
        List<String> list = Helper.getAsList(multipartFile);
        this.counter = 0;
        this.circleList = breakToLists(list,circleList,"چرخه دوم");
        saveReturnRegularTransaction(circleList,1);
        this.circleList = breakToLists(list,circleList,"چرخه سوم");
        saveReturnRegularTransaction(circleList,2);
        this.circleList = breakToLists(list,circleList,"چرخه چهارم");
        saveReturnRegularTransaction(circleList,3);
        this.circleList = breakToLists(list,circleList," ");
        saveReturnRegularTransaction(circleList,4);
    }

    public void saveReturnRegularTransaction (List<String> list, Integer circleNo) {

        for (int i = 0; i < list.size(); i = i + 13) {
            ReturnedRegularTransaction rrt = new ReturnedRegularTransaction();
            rrt.setReturnedId(list.get(i));
            rrt.setSenderFullName(list.get(i + 1));
            rrt.setSenderId(list.get(i + 2));
            rrt.setSenderAccountNumber(list.get(i + 3));
            rrt.setRemittingBank(list.get(i + 4));
            rrt.setReceivingBank(list.get(i + 5));
            rrt.setReceiverFullName(list.get(i + 6));
            rrt.setReceiverBankAccount(list.get(i + 7));
            rrt.setPrimitiveTrackingNumber(list.get(i + 8));
            rrt.setPrimitiveTransactionId(list.get(i + 9));
            rrt.setPrimitiveAmount(Long.parseLong(list.get(i + 10)));
            rrt.setReturnedAmount(Long.parseLong(list.get(i + 11)));
            rrt.setBankBranchCode(list.get(i + 12));
            rrt.setCircle(circleNo);
            returnedRegularTransactionDao.save(rrt);
        }
    }

    public void breakReturnedShaparakTransaction (MultipartFile multipartFile) {
        List<String> list = Helper.getAsList(multipartFile);
        this.counter = 0;
        this.circleList = breakToLists(list,circleList,"چرخه دوم");
        saveReturnedShaparakTransaction(circleList,1);
        this.circleList = breakToLists(list,circleList,"چرخه سوم");
        saveReturnedShaparakTransaction(circleList,2);
        this.circleList = breakToLists(list,circleList,"چرخه چهارم");
        saveReturnedShaparakTransaction(circleList,3);
        this.circleList = breakToLists(list,circleList," ");
        saveReturnedShaparakTransaction(circleList,4);
    }

    public void saveReturnedShaparakTransaction (List<String> list, Integer circleNo) {

        for (int i = 0; i < list.size(); i = i + 12) {
            ReturnedShaparakTransaction rst = new ReturnedShaparakTransaction();
            rst.setReturnedId(list.get(i));
            rst.setSenderFullName(list.get(i + 1));
            rst.setSenderId(list.get(i + 2));
            rst.setSenderAccountNumber(list.get(i + 3));
            rst.setRemittingBank(list.get(i + 4));
            rst.setReceivingBank(list.get(i + 5));
            rst.setReceiverFullName(list.get(i + 6));
            rst.setReceiverBankAccount(list.get(i + 7));
            rst.setPrimitiveTrackingNumber(list.get(i + 8));
            rst.setPrimitiveTransactionId(list.get(i + 9));
            rst.setPrimitiveAmount(Long.parseLong(list.get(i + 10)));
            rst.setReturnedAmount(Long.parseLong(list.get(i + 11)));
            rst.setCircle(circleNo);
            returnedShaparakTransactionDao.save(rst);
        }
    }

    public List<String> breakToLists(List<String> baseList, List<String> newList, String circle) {
        newList.clear();
        System.out.println(baseList.remove("چرخه اول"));
        for (int i = this.counter; i < baseList.size(); i++) {
            System.out.println(baseList.get(i));
            if (baseList.get(i).equals(circle)) {
                baseList.remove(circle);
                break;
            }
            this.counter++;
            newList.add(baseList.get(i));
        }
        return newList;
    }

    public List<String> addFlagToList (List<String> list, Integer i, String flag) {
        for (int j = i; j < list.size(); j = j + i) {
            list.add(j,flag);
        }
        return list;
    }



}
