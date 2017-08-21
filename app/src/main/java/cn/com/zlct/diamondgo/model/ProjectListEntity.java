package cn.com.zlct.diamondgo.model;

import java.util.List;

/**
 * 夺宝项目列表信息 实体类
 */
public class ProjectListEntity {

    /**
     * ErrNum : 0
     * ErrMsg : 获取成功！
     * data :
     */

    private String ErrNum;
    private String ErrMsg;
    /**
     * totalRecords : 100
     * curPage : 1
     * Project :
     */

    private List<DataEntity> data;

    public String getErrNum() {
        return ErrNum;
    }

    public void setErrNum(String ErrNum) {
        this.ErrNum = ErrNum;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String totalRecords;
        private String curPage;
        /**
         * ProjectId : 5919bafcd25e48c9b8a0a1490acc513a
         * BasicInformationId : 36d72e1c01e94913b3b49672364948d0
         * Number : 第1期
         * Title : Apple iPhone 7 (A1660) 128G 黑色 移动联通电信4G手机
         * ItemAmount : 6588.00
         * UnitPrice : 1.00
         * Copies : 6588
         * BuyCopies : 0
         * IsRestriction : 1
         * IsNewhand : 0
         * RestrictionCount : 5
         * Mark : 0
         * ReferenceNumber : 0
         * LotteryDate :
         * SumDate :
         * LuckyNumber :
         * Speed : 0.00
         * CreateDate : 2016/10/13 16:10:21
         * SortCode : 1
         * Synopsis :
         * FilePath : /Resource/MerchandiseFile/20161014/20161014054554.jpg
         * ItemType : 0b741525b84d459e9bae176f29124811
         * IsMall : 1
         * LuckUserData : []
         */

        private List<ProjectEntity> Project;

        public String getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(String totalRecords) {
            this.totalRecords = totalRecords;
        }

        public String getCurPage() {
            return curPage;
        }

        public void setCurPage(String curPage) {
            this.curPage = curPage;
        }

        public List<ProjectEntity> getProject() {
            return Project;
        }

        public void setProject(List<ProjectEntity> Project) {
            this.Project = Project;
        }

        public static class ProjectEntity {
            private String ProjectId;
            private String BasicInformationId;
            private String Number;
            private String Title;
            private String ItemAmount;
            private String UnitPrice;
            private String Copies;
            private String BuyCopies;
            private String IsRestriction;
            private String IsNewhand;
            private String RestrictionCount;
            private String Mark;
            private String ReferenceNumber;
            private String LotteryDate;
            private String SumDate;
            private String LuckyNumber;
            private String Speed;
            private String CreateDate;
            private String SortCode;
            private String Synopsis;
            private String FilePath;
            private String ItemType;
            private String IsMall;
            private int type;

            public ProjectEntity(int type) {
                this.type = type;
            }

            public void upNew(String mark, String lotteryDate, String luckyNumber){
                Mark = mark;
                LotteryDate = lotteryDate;
                LuckyNumber = luckyNumber;
            }

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public String getBasicInformationId() {
                return BasicInformationId;
            }

            public void setBasicInformationId(String BasicInformationId) {
                this.BasicInformationId = BasicInformationId;
            }

            public String getNumber() {
                return Number;
            }

            public void setNumber(String Number) {
                this.Number = Number;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public String getItemAmount() {
                return ItemAmount;
            }

            public void setItemAmount(String ItemAmount) {
                this.ItemAmount = ItemAmount;
            }

            public String getUnitPrice() {
                return UnitPrice;
            }

            public void setUnitPrice(String UnitPrice) {
                this.UnitPrice = UnitPrice;
            }

            public String getCopies() {
                return Copies;
            }

            public void setCopies(String Copies) {
                this.Copies = Copies;
            }

            public String getBuyCopies() {
                return BuyCopies;
            }

            public void setBuyCopies(String BuyCopies) {
                this.BuyCopies = BuyCopies;
            }

            public String getIsRestriction() {
                return IsRestriction;
            }

            public void setIsRestriction(String IsRestriction) {
                this.IsRestriction = IsRestriction;
            }

            public String getIsNewhand() {
                return IsNewhand;
            }

            public void setIsNewhand(String IsNewhand) {
                this.IsNewhand = IsNewhand;
            }

            public String getRestrictionCount() {
                return RestrictionCount;
            }

            public void setRestrictionCount(String RestrictionCount) {
                this.RestrictionCount = RestrictionCount;
            }

            public String getMark() {
                return Mark;
            }

            public void setMark(String Mark) {
                this.Mark = Mark;
            }

            public String getReferenceNumber() {
                return ReferenceNumber;
            }

            public void setReferenceNumber(String ReferenceNumber) {
                this.ReferenceNumber = ReferenceNumber;
            }

            public String getLotteryDate() {
                return LotteryDate;
            }

            public void setLotteryDate(String LotteryDate) {
                this.LotteryDate = LotteryDate;
            }

            public String getSumDate() {
                return SumDate;
            }

            public void setSumDate(String SumDate) {
                this.SumDate = SumDate;
            }

            public String getLuckyNumber() {
                return LuckyNumber;
            }

            public void setLuckyNumber(String LuckyNumber) {
                this.LuckyNumber = LuckyNumber;
            }

            public String getSpeed() {
                return Speed;
            }

            public void setSpeed(String Speed) {
                this.Speed = Speed;
            }

            public String getCreateDate() {
                return CreateDate;
            }

            public void setCreateDate(String CreateDate) {
                this.CreateDate = CreateDate;
            }

            public String getSortCode() {
                return SortCode;
            }

            public void setSortCode(String SortCode) {
                this.SortCode = SortCode;
            }

            public String getSynopsis() {
                return Synopsis;
            }

            public void setSynopsis(String Synopsis) {
                this.Synopsis = Synopsis;
            }

            public String getFilePath() {
                return FilePath;
            }

            public void setFilePath(String FilePath) {
                this.FilePath = FilePath;
            }

            public String getItemType() {
                return ItemType;
            }

            public void setItemType(String ItemType) {
                this.ItemType = ItemType;
            }

            public String getIsMall() {
                return IsMall;
            }

            public void setIsMall(String IsMall) {
                this.IsMall = IsMall;
            }



            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
