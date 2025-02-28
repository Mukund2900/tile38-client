package com.mapabc.commands;

import io.lettuce.core.protocol.ProtocolKeyword;

import java.nio.charset.StandardCharsets;

public enum BatchedCommandType implements ProtocolKeyword {
    ACL,
    AUTH,
    AUTH2,
    ECHO,
    HELLO,
    PING,
    QUIT,
    READONLY,
    READWRITE,
    SELECT,
    SWAPDB,
    BGREWRITEAOF,
    BGSAVE,
    CLIENT,
    COMMAND,
    CONFIG,
    DBSIZE,
    DEBUG,
    FLUSHALL,
    FLUSHDB,
    INFO,
    MYID,
    LASTSAVE,
    ROLE,
    MONITOR,
    SAVE,
    SHUTDOWN,
    SLAVEOF,
    SLOWLOG,
    SYNC,
    MEMORY,
    COPY,
    DEL,
    DUMP,
    EXISTS,
    EXPIRE,
    EXPIREAT,
    KEYS,
    MIGRATE,
    MOVE,
    OBJECT,
    PERSIST,
    PEXPIRE,
    PEXPIREAT,
    PTTL,
    RANDOMKEY,
    RENAME,
    RENAMENX,
    RESTORE,
    TOUCH,
    TTL,
    TYPE,
    SCAN,
    UNLINK,
    APPEND,
    GET,
    GETDEL,
    GETEX,
    GETRANGE,
    GETSET,
    MGET,
    MSET,
    MSETNX,
    SET,
    SETEX,
    PSETEX,
    SETNX,
    SETRANGE,
    STRLEN,
    STRALGO,
    DECR,
    DECRBY,
    INCR,
    INCRBY,
    INCRBYFLOAT,
    BLMOVE,
    BLPOP,
    BRPOP,
    BRPOPLPUSH,
    LINDEX,
    LINSERT,
    LLEN,
    LMOVE,
    LPOP,
    LPOS,
    LPUSH,
    LPUSHX,
    LRANGE,
    LREM,
    LSET,
    LTRIM,
    RPOP,
    RPOPLPUSH,
    RPUSH,
    RPUSHX,
    SORT,
    HDEL,
    HEXISTS,
    HGET,
    HGETALL,
    HINCRBY,
    HINCRBYFLOAT,
    HKEYS,
    HLEN,
    HSTRLEN,
    HMGET,
    HMSET,
    HRANDFIELD,
    HSET,
    HSETNX,
    HVALS,
    HSCAN,
    DISCARD,
    EXEC,
    MULTI,
    UNWATCH,
    WATCH,
    PFADD,
    PFCOUNT,
    PFMERGE,
    PSUBSCRIBE,
    PUBLISH,
    PUNSUBSCRIBE,
    SUBSCRIBE,
    UNSUBSCRIBE,
    PUBSUB,
    SADD,
    SCARD,
    SDIFF,
    SDIFFSTORE,
    SINTER,
    SINTERSTORE,
    SISMEMBER,
    SMISMEMBER,
    SMEMBERS,
    SMOVE,
    SPOP,
    SRANDMEMBER,
    SREM,
    SSCAN,
    SUNION,
    SUNIONSTORE,
    BZPOPMIN,
    BZPOPMAX,
    ZADD,
    ZCARD,
    ZCOUNT,
    ZDIFF,
    ZDIFFSTORE,
    ZINCRBY,
    ZINTER,
    ZINTERSTORE,
    ZLEXCOUNT,
    ZMSCORE,
    ZPOPMIN,
    ZPOPMAX,
    ZRANDMEMBER,
    ZRANGE,
    ZRANGEBYSCORE,
    ZRANGESTORE,
    ZRANK,
    ZREM,
    ZREMRANGEBYRANK,
    ZREMRANGEBYSCORE,
    ZREVRANGE,
    ZREVRANGEBYLEX,
    ZREVRANGEBYSCORE,
    ZREVRANK,
    ZSCAN,
    ZSCORE,
    ZUNION,
    ZUNIONSTORE,
    ZREMRANGEBYLEX,
    ZRANGEBYLEX,
    EVAL,
    EVALSHA,
    SCRIPT,
    BITCOUNT,
    BITFIELD,
    BITOP,
    GETBIT,
    SETBIT,
    BITPOS,
    GEOADD,
    GEODIST,
    GEOHASH,
    GEOENCODE,
    GEODECODE,
    GEOPOS,
    GEORADIUS,
    GEORADIUS_RO,
    GEORADIUSBYMEMBER,
    GEORADIUSBYMEMBER_RO,
    GEOSEARCH,
    GEOSEARCHSTORE,
    XACK,
    XADD,
    XAUTOCLAIM,
    XCLAIM,
    XDEL,
    XGROUP,
    XINFO,
    XLEN,
    XPENDING,
    XRANGE,
    XREVRANGE,
    XREAD,
    XREADGROUP,
    XTRIM,
    TIME,
    WAIT,
    SENTINEL,
    ASKING,
    NEARBY,
    WITHIN,
    INTERSECTS,
    SETHOOK,
    CLUSTER;
    public final byte[] bytes;

    private BatchedCommandType() {
        this.bytes = this.name().getBytes(StandardCharsets.US_ASCII);
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
