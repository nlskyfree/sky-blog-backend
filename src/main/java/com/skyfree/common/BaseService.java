package com.skyfree.common;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


@SuppressWarnings("unchecked")
public class BaseService<T> {
    @Autowired
    @Qualifier("sqlLiteJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("sqlLiteNamedJdbcTemplate")
    protected NamedParameterJdbcTemplate namedJdbcTemplate;
    
    private static final String SQL_QUERY = "query";
    private static final String SQL_INSERT = "insert";
    private static final String SQL_UPDATE = "update";
    // private static final String SQL_DELETE = "delete";
    
    // 参数化类型的class
    private Class<?> entityClass;
    // 根据参数化类型得到的表名称
    private String tbName;
    // 主键
    private Field primaryField;
    // 主键是否自增
    private boolean isIncremental;
    // entityClass对应的所有字段(包含私有字段和所有父类字段)
    private Set<Field> fields;
    // entityClass 对应的所有属性字段名称去重后的Set(包含私有字段名称和所有父类字段的名称)
    private Set<String> fieldNameSet;
    
    public BaseService() {
        // 目的：得到实际类型参数
        // 得到当前运行对象
        Class<?> clazz = this.getClass();
        // 得到当前对象父类的参数化类型,一般使用type子接口ParameterizedType
        Type type = clazz.getGenericSuperclass();
        ParameterizedType ptype = (ParameterizedType) type;
        // 得到实际类型参数
        Type[] types = ptype.getActualTypeArguments();
        this.entityClass = (Class<?>) types[0];
        this.fields = new HashSet<>();
        this.fieldNameSet = new HashSet<String>();
        // 如果有@TableName注解，取其值作为表名称，如果没有自动取类型名称转换为表名称
        TableName tableName = this.entityClass.getAnnotation(TableName.class);
        if(!GyUtils.isNull(tableName) && !GyUtils.isNull(tableName.value())) {
            this.tbName = tableName.value();
        } else {
            this.tbName = NameTransferUtils.camelCast2Underline(entityClass.getSimpleName());
        }
        
        List<Field> primaryFields = new ArrayList<>();
        Class<?> tempClass = entityClass;
        // 当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            List<Field> fieldList = Arrays.asList(tempClass.getDeclaredFields());
            if (!GyUtils.isNull(fieldList)) {
                for (int i = 0; i < fieldList.size(); i++) {
                    Field field = fieldList.get(i);
                    // logger.debug("field.getName():{}",field.getName());
                    if (!this.fieldNameSet.contains(field.getName())) {
                        this.fieldNameSet.add(field.getName());
                        this.fields.add(field);
                    }
                    
                    PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                    if(!GyUtils.isNull(primaryKey)) {
                        primaryFields.add(field);
                        this.isIncremental = primaryKey.isIncremental();
                    }
                }
            }
            tempClass = tempClass.getSuperclass();
        }
        
        
        if(GyUtils.isNull(primaryFields) || primaryFields.size() != 1) {
            throw new SQLException("数据库entity必须有且仅有一个字段注解为@PrimaryKey！");
        }
        
        this.primaryField = primaryFields.get(0);
    }
    
    /**
     * @description 根据所有entity不为空的字段作为查询条件查询
     * @time 创建时间:2017年9月29日下午12:15:30
     * @param entity
     * @return
     * @author 倪路
     */
    public List<T> find(T entity) {
        return findOrderBy(entity, null);
    }
    
    /**
     * @description 查找并排序
     * @time 创建时间:2017年12月2日上午11:35:40
     * @param entity
     * @param orderByFieldName
     * @return
     * @author 倪路
     */
    public List<T> findOrderBy(T entity, String orderByFieldName) {
        List<T> result = null;
        try {
            RowMapper<T> rowMapper = (RowMapper<T>) BeanPropertyRowMapper.newInstance(entityClass);
            if(GyUtils.isNull(entity)) {
                String sql = "SELECT * FROM " + this.tbName;
                return jdbcTemplate.query(sql, rowMapper);
            }
            String sql = this.makeSql(entity, SQL_QUERY);
            
            // 如果要求排序，并且排序字段存在表中
            if(!GyUtils.isNull(orderByFieldName) && fieldNameSet.contains(orderByFieldName)) {
                // 转下划线命名法
                orderByFieldName = NameTransferUtils.camelCast2Underline(orderByFieldName);
                sql = sql + " order by " + orderByFieldName;
            }
            Object[] params = this.setArgs(entity, SQL_QUERY);
            result = jdbcTemplate.query(sql,  params, rowMapper);
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return result;
    }

    /**
     * @description 根据所有entity不为空的字段作为查询条件查询一条数据
     * @time 创建时间:2017年9月29日下午12:16:03
     * @param entity
     * @return
     * @author 倪路
     */
    public T findOne(T entity) {
        List<T> list = this.find(entity);
        return GyUtils.isNull(list) ? null : list.get(0);
    }

    /**
     * @description 根据主键查询实体
     * @time 创建时间:2017年9月29日下午12:16:24
     * @param id
     * @return
     * @author 倪路
     */
    public T findById(Object id) {
        Object obj = null;
        try {
            obj = entityClass.newInstance();
            primaryField.setAccessible(true);
            primaryField.set(obj, id);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SQLException(e);
        }
        List<T> result = this.find((T) obj);
        return GyUtils.isNull(result) ? null : result.get(0);
    }

    /**
     * @description 查找表所有数据
     * @time 创建时间:2017年9月29日下午12:16:43
     * @return
     * @author 倪路
     */
    public List<T> findAll() {
        return this.find(null);
    }

    /**
     * @description 根据实体所有不为空字段更新数据
     * @time 创建时间:2017年9月29日下午12:17:14
     * @param entity
     * @return
     * @author 倪路
     */
    public int update(T entity) {
        return this.update(entity, false);
    }

    /**
     * @description 更新实体，isForce为true，null字段也更新，为false，null字段不更新
     * @time 创建时间:2017年9月29日下午12:18:53
     * @param entity
     * @param isForce
     * @return
     * @author 倪路
     */
    public int update(T entity, boolean isForce) {
        int result = -1;
        try {
            result = jdbcTemplate.update(this.makeSql(entity, SQL_UPDATE),
                    this.setArgs(entity, SQL_UPDATE));
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return result;
    }

    /**
     * @description 插入一条记录
     * @time 创建时间:2017年9月29日下午12:19:24
     * @param entity
     * @return
     * @author 倪路
     */
    public int insert(T entity) {
        int generatedKey = -1;
        try {
            String sql = this.makeSql(entity, SQL_INSERT);
            Object[] args = this.setArgs(entity, SQL_INSERT);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update((conn)-> {
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for(int i = 1 ; i <= args.length ; i++) {
                    ps.setObject(i, args[i - 1]);
                }
                return ps;
            }, keyHolder);
            generatedKey = keyHolder.getKey().intValue();
        } catch (Exception e) {
            throw new SQLException(e);
        }
        return generatedKey;
    }

    /**
     * @description 保存数据，更新entity的id字段是否为空判断是插入还是更新
     * @time 创建时间:2017年9月29日下午12:19:33
     * @param entity
     * @return
     * @author 倪路
     */
    public int save(T entity) {
        Object id = null;
        try {
            this.primaryField.setAccessible(true);
            id = this.primaryField.get(entity);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new SQLException(e);
        }
        return GyUtils.isNull(id) ? this.insert(entity) : this.update(entity);
    }

    /**
     * 
     * @description 根据id进行逻辑删除
     * @time 创建时间:2017年9月25日下午5:25:49
     * @param id 主键
     * @author 倪路
     */
    public int deleteById(Integer id) {
        return deleteById(id, false);
    }

    /**
     * @description 根据id删除数据，isDelete为true代表物理删除，为false代表逻辑删除
     * @time 创建时间:2017年9月29日下午12:22:50
     * @param id
     * @param isDelete
     * @return
     * @author 倪路
     */
    public int deleteById(Integer id, boolean isDelete) {
        try {
            return isDelete ? jdbcTemplate.update("delete from " + this.tbName + " where id = ?", id)
                : jdbcTemplate.update("update " + this.tbName + " set is_valid = 1 where id = ?", id);
        } catch(Exception e) {
            throw new SQLException(e);
        }
    }

    private String makeSql(T entity, String sqlFlag)
            throws IllegalArgumentException, IllegalAccessException {
        return this.makeSql(entity, sqlFlag, false);
    }

    // 组装SQL
    private String makeSql(T entity, String sqlFlag, boolean isForce)
            throws IllegalArgumentException, IllegalAccessException {
        StringBuilder sql = new StringBuilder();
        if (sqlFlag.equals(SQL_QUERY)) {
            sql.append("select * from ").append(this.tbName).append(" where ");
            Object value;
            if (!GyUtils.isNull(entity) && !GyUtils.isNull(fields)) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    value = field.get(entity);
                    if (!GyUtils.isNull(value)) {
                        sql.append(NameTransferUtils.camelCast2Underline(field.getName()))
                                .append("=?").append(" and ");
                    }
                }
                sql.delete(sql.length() - 4, sql.length());
            }
            
            String temp = sql.toString();
            // 如果没有任何查询条件，去掉where
            if(temp.endsWith(" where ")) {
                sql.delete(sql.length() - 6, sql.length());
            }
        } else if (sqlFlag.equals(SQL_INSERT)) {
            sql.append(" insert into ");
            sql.append(this.tbName);
            sql.append("(");
            for (Field field : fields) {
                field.setAccessible(true); // 暴力反射
                String column = field.getName();
                // 自增主键，则跳过
                if (field.equals(primaryField) && this.isIncremental) {
                    continue;
                }

                sql.append(NameTransferUtils.camelCast2Underline(column)).append(",");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(") values (");
            for (Field field : fields) {
                field.setAccessible(true); // 暴力反射
                if (field.equals(primaryField) && this.isIncremental) {
                    continue;
                }
                sql.append("?,");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
        } else if (sqlFlag.equals(SQL_UPDATE)) {
            sql.append(" UPDATE " + this.tbName + " SET ");
            for (Field field : fields) {
                field.setAccessible(true); // 暴力反射
                String name = field.getName();
                Object value = field.get(entity);
                // 不update id,且为空的值不update
                if (field.equals(primaryField) || GyUtils.isNull(value)) {
                    continue;
                }
                sql.append(NameTransferUtils.camelCast2Underline(name)).append("=").append("?,");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
            String primaryName = NameTransferUtils.camelCast2Underline(primaryField.getName());
            sql.append(" where " + primaryName + "=?");
        }
        return sql.toString();
    }

    private Object[] setArgs(T entity, String sqlFlag)
            throws IllegalArgumentException, IllegalAccessException {
        return this.setArgs(entity, sqlFlag, false);
    }

    // 设置参数
    private Object[] setArgs(T entity, String sqlFlag, boolean isForce)
            throws IllegalArgumentException, IllegalAccessException {
        List<Object> args = new ArrayList<>();
        if (sqlFlag.equals(SQL_QUERY)) {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (!GyUtils.isNull(value)) {
                    args.add(value);
                }
            }
        } else if (sqlFlag.equals(SQL_INSERT)) {
            for (Field field : fields) {
                // 假设数据库表使用自增id
                if (field.equals(primaryField) && this.isIncremental) {
                    continue;
                }
                args.add(field.get(entity));
            }
        } else if (sqlFlag.equals(SQL_UPDATE)) {
            Object id = null;
            for (Field field : fields) {
                field.setAccessible(true); // 暴力反射
                Object value = field.get(entity);
                if (field.equals(primaryField)) {
                    id = value;
                    continue;
                }
                if (!GyUtils.isNull(value)) {
                    args.add(value);
                }
            }
            // id做为最后一个参数的where条件
            args.add(id);
        }
        return args.toArray();
    }
    
    public int getLastInsertId() {
        String sql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
