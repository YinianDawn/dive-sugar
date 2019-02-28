package dive.sugar;

/**
 * @author: dawn
 */
public enum Auto {
    /**
     * 每次删除重建
     */
    REREATE,
    /**
     * 仅不存在创建
     */
    CREATE,
    /**
     * 若不一致，则更新
     */
    UPDATE
}
