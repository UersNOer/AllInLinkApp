package com.unistrong.api.services.cloud;


/**
 * 云图数据存储中创建数据集时的字段属性类
 */
public class DBFieldInfo {
    /**
     * 字段id.
     */
    private int id;
    /**
     * 字段存储名称
     */
    private String fieldName;
    /**
     * 字段显示名称
     */
    private String fieldTitle;
    /**
     * 字段长度
     */
    private int fieldSize;
    /**
     * 字段类型
     */
    private FieldType fieldType;
    /**
     * 最后一次编辑时间
     */
    private long editTime;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 是否建索引（0否、1是）
     */
    private int dbindex;


    /**
     * 构造方法
     * @param fieldName 字段存储名称
     * @param fieldTitle 字段显示名称
     * @param fieldType 字段类型
     * @param dbindex 是否建立索引 0,否;1,是
     */
    public DBFieldInfo(String fieldName,String fieldTitle,FieldType fieldType,int dbindex){
        this.fieldName=fieldName;
        this.fieldTitle=fieldTitle;
        this.fieldType=fieldType;
        this.dbindex=dbindex;
    }

    /**
     * 构造方法
     */
    public DBFieldInfo(){

    }

    /**
     * 获取字段id.
     * @return 字段id.
     */
    public int getId() {
        return id;
    }

    /**
     * 设置字段id。
     * @param id 字段id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取字段存储名称。
     * @return 字段存储名称。
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 设置字段存储名称。
     * @param fieldName 字段存储名称。
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 获取字段显示名称。
     * @return 字段显示名称。
     */
    public String getFieldTitle() {
        return fieldTitle;
    }

    /**
     * 设置字段显示名称。
     * @param fieldTitle 字段显示名称。
     */
    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    /**
     * 获取字段长度。
     * @return 字段长度。
     */
    public int getFieldSize() {
        return fieldSize;
    }

    /**
     * 设置字段长度。
     * @param fieldSize 字段长度。
     */
    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    /**
     * 获取字段类型。
     * @return 字段类型。
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * 设置字段类型。
     * @param fieldType 字段类型。
     */
    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * 获取最后一次编辑时间。
     * @return 最后一次编辑时间
     */
    public long getEditTime() {
        return editTime;
    }

    /**
     * 设置最后一次编辑时间。
     * @param editTime 最后一次编辑时间。
     */
    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }

    /**
     * 获取创建时间。
     * @return 创建时间。
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间。
     * @param createTime 创建时间。
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否建立索引。
     * @return 0,否;1,是。
     */
    public int getDbindex() {
        return dbindex;
    }

    /**
     * 设置是否建立索引。
     * @param dbindex  0,否;1,是。
     */
    public void setDbindex(int dbindex) {
        this.dbindex = dbindex;
    }

    /**
     * 字段类型枚举类
     */
    public  static enum FieldType{
        /**
         * varchar 类型
         */
       type_varchar("varchar"),
        /**
         * long 类型
         */
        type_long("long"),
        /**
         * integer 类型
         */
        type_integer("integer"),
        /**
         *  double 类型
         */
        type_double("double"),
        /**
         * text 类型
         */
        type_text("text");

        private String type="";
        private FieldType(String type){
            this.type=type;
        }

        /**
         * 获取类型的字符串值
         * @return 类型的字符串值
         */
        public String getTypeString(){
            return this.type;
        }
    }
}
