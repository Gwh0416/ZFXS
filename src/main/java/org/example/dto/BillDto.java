package org.example.dto;

import org.example.entity.Bill;

public class BillDto extends Bill {

    private String DiaryName;

    private String KindName;

    public String getDiaryName() {
        return DiaryName;
    }

    public void setDiaryName(String diaryName) {
        DiaryName = diaryName;
    }

    public String getKindName() {
        return KindName;
    }

    public void setKindName(String kindName) {
        KindName = kindName;
    }
}
