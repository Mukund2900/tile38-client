package com.mapabc.api;

import com.alibaba.fastjson.JSONObject;
import com.mapabc.client.Tile38Client;
import com.mapabc.commands.BatchedCommandType;
import com.mapabc.commands.Tile38Commands;
import com.mapabc.entity.*;
import com.mapabc.eunms.DetectType;
import com.mapabc.util.StringUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ArrayOutput;
import io.lettuce.core.protocol.CommandArgs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author ke.han
 * @Date 2019/11/13 13:52
 **/
public class Tile38TemplateImpl implements Tile38Template {

    private Tile38Commands commands = null;

    private RedisClient client = null;

    private Tile38Client tile38Client = null;

    @Override
    public Tile38Commands getCommands() {
        return commands;
    }

    @Override
    public RedisClient getClient() {
        return client;
    }

    public void close() {
        if (tile38Client != null) {
            tile38Client.close();
        }
    }

    public Tile38TemplateImpl(Tile38Client tile38Client) {
        this.tile38Client = tile38Client;
        this.commands = tile38Client.getCommands();
        this.client = tile38Client.getRedisClient();
    }

    @Override
    public String auth(String password) {
        if (StringUtil.isBlack(password)) {
            return "password is not null";
        }
        return commands.auth(password);
    }

    @Override
    public void flushDB() {
        commands.flushDB();
    }

    @Override
    public void gc() {
        commands.gc();
    }

    @Override
    public String outPut(String outPut) {
        if (StringUtil.isBlack(outPut)) {
            return "outPut is not null";
        }
        return commands.outPut(outPut);
    }

    @Override
    public String follow(String ip, int port) {
        if (StringUtil.isBlack(ip)) {
            return "ip is not null";
        }
        return commands.follow(ip, port);
    }

    @Override
    public void quit() {
        commands.quit();
    }

    @Override
    public void aofShrink() {
        commands.aofShrink();
    }

    @Override
    public String delEntry(String key, String member) {
        if (StringUtil.isBlack(key) || StringUtil.isBlack(member)) {
            return "key is not null";
        }
        return commands.delEntry(key,member);
    }

    @Override
    public String intersects(String key, Element element) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        if (element == null) {
            return "element is not null";
        }
        if (element instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) element;
            Double lng1 = rectangle.getLeftDown().getLng();
            Double lat1 = rectangle.getLeftDown().getLat();
            Double lng2 = rectangle.getRightUpper().getLng();
            Double lat2 = rectangle.getRightUpper().getLat();
            return commands.intersects(key, lng1, lat1, lng2, lat2);
        } else {
            Fence fence = (Fence) element;
            return commands.intersects(key, JSONObject.toJSONString(fence.getFence()));
        }
    }

    @Override
    public String nearBy(String key, double lng, double lat, int length) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        //此处 lat,lng 坐标反转是为了匹配tile38坐标顺序格式
        return commands.nearBy(key, lat, lng, length);
    }

    @Override
    public String nearByWithDistance(String key, double lng, double lat, int length) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        //此处 lat,lng 坐标反转是为了匹配tile38坐标顺序格式
        return commands.nearByWithDistance(key, lat, lng, length);
    }
    @Override
    public String nearByLimit(String key, int limit, double lng, double lat) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        //此处 lat,lng 坐标反转是为了匹配tile38坐标顺序格式
        return commands.nearByLimit(key, limit, lat, lng);
    }

    @Override
    public String getKeys(String key) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        return commands.getKeys(key);
    }

    @Override
    public String scanObjInKey(String key) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        return commands.scanObjInKey(key);
    }

    @Override
    public String scanObjInKeyByTime(String key, long startTime, long endTime, int limit, String orderBy) {
        if (StringUtil.isBlack(key, orderBy)) {
            return "key or orderBy is not null";
        }
        return commands.scanObjInKeyByTime(key, startTime, endTime, limit, orderBy);
    }

    @Override
    public String withIn(String key, Element element) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        if (element instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) element;
            Double lng1 = rectangle.getLeftDown().getLng();
            Double lat1 = rectangle.getLeftDown().getLat();
            Double lng2 = rectangle.getRightUpper().getLng();
            Double lat2 = rectangle.getRightUpper().getLat();
            return commands.withIn(key, lng1, lat1, lng2, lat2);
        } else if (element instanceof Sector) {
            Sector sector = (Sector) element;
            double lng = sector.getLng();
            double lat = sector.getLat();
            int r = sector.getR();
            int startAngle = sector.getStartAngle();
            int endAngle = sector.getEndAngle();
            return commands.withIn(key, lng, lat, r, startAngle, endAngle);
        } else {
            Fence fence = (Fence) element;
            return commands.withIn(key, JSONObject.toJSONString(fence.getFence()));
        }
    }

    @Override
    public String withIn(String key, int limit, long startTime, long endTime, String key1, String id) {
        if (StringUtil.isBlack(key, key1, id)) {
            return "key key1 id is not null";
        }
        return commands.withIn(key, limit, startTime, endTime, key1, id);
    }

    @Override
    public String getMinBounds(String key) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        return commands.getMinBounds(key);
    }

    @Override
    public String statsKeys(String keys) {
        if (StringUtil.isBlack(keys)) {
            return "keys is not null";
        }
        return commands.statsKeys(keys);
    }

    @Override
    public String getElement(String key, String id) {
        if (StringUtil.isBlack(key, id)) {
            return "key or id is not null";
        }
        return commands.getElement(key, id);
    }

    @Override
    public String setElement(String key, String id, Element element) {
        if (StringUtil.isBlack(key, id)) {
            return "key or id is not null";
        }
        if (element == null) {
            return "element is not null";
        }
        if (element instanceof Point) {
            Point point = (Point) element;
            return commands.setElement(key, id, point.getLng(), point.getLat());
        } else if (element instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) element;
            Double lng1 = rectangle.getLeftDown().getLng();
            Double lat1 = rectangle.getLeftDown().getLat();
            Double lng2 = rectangle.getRightUpper().getLng();
            Double lat2 = rectangle.getRightUpper().getLat();
            return commands.setElement(key, id, lng1, lat1, lng2, lat2);
        } else {
            Fence fence = (Fence) element;
            return commands.setElement(key, id, JSONObject.toJSONString(fence.getFence()));
        }

    }

    @Override
    public String setElement(String key, String id, String geojson) {
        return commands.setElement(key, id, geojson);
    }

    @Override
    public String setElementWithField(String key, String id, String geojson , String fieldKeyName , String fieldValue) {
        return commands.setElementWithField(key, id, geojson , fieldKeyName , fieldValue);
    }

    @Override
    public String setElementWithFields(String key, String id, String geojson , String field1KeyName , String field1Value , String field2KeyName , String field2Value) {
        return commands.setElementWithFields(key, id, geojson , field1KeyName , field1Value , field2KeyName , field2Value);
    }

    @Override
    public String dropObjInKey(String key) {
        if (StringUtil.isBlack(key)) {
            return "key is not null";
        }
        return commands.dropObjInKey(key);
    }

    @Override
    public String pDelObjInKey(String key, String id) {
        if (StringUtil.isBlack(key, id)) {
            return "key or id is not null";
        }
        return commands.pDelObjInKey(key, id);
    }

    @Override
    public String replaceKey(String key, String newKey) {
        if (StringUtil.isBlack(key, newKey)) {
            return "key or newKey is not null";
        }
        return commands.replaceKey(key, newKey);
    }

    @Override
    public String updateKey(String key, String newKey) {
        if (StringUtil.isBlack(key, newKey)) {
            return "key or newKey is not null";
        }
        return commands.updateKey(key, newKey);
    }

    @Override
    public String setExpireIdInKey(String key, String id, int time) {
        if (StringUtil.isBlack(key, id)) {
            return "key or id is not null";
        }
        return commands.setExpireIdInKey(key, id, time);
    }

    @Override
    public String delExpireIdInKey(String key, String id) {
        if (StringUtil.isBlack(key, id)) {
            return "key or id is not null";
        }
        return commands.delExpireIdInKey(key, id);
    }

    @Override
    public String getExpireIdInKey(String key, String id) {
        if (StringUtil.isBlack(key, id)) {
            return "key or id is not null";
        }
        return commands.getExpireIdInKey(key, id);
    }

    @Override
    public String jSet(String gJson) {
        if (StringUtil.isBlack(gJson)) {
            return "gJson is not null";
        }
        return commands.jSet(gJson);
    }

    @Override
    public String jGet(String gJson) {
        if (StringUtil.isBlack(gJson)) {
            return "gJson is not null";
        }
        return commands.jGet(gJson);
    }

    @Override
    public String jDel(String gJson) {
        if (StringUtil.isBlack(gJson)) {
            return "gJson is not null";
        }
        return commands.jDel(gJson);
    }

    @Override
    public String setChanWithIn(String chanName, String key, String detect, String geoJson) {
        if (StringUtil.isBlack(chanName, key)) {
            return "chanName or key is not null";
        }
        if (detect == null) {
            return "detect is not null";
        }
        return commands.setChanWithIn(chanName, key, detect, geoJson);
    }


    @Override
    public String setChan(String chanName, String key, DetectType detect, double lng, double lat, int length) {
        if (StringUtil.isBlack(chanName, key)) {
            return "chanName or key is not null";
        }
        if (detect == null) {
            return "detect is not null";
        }
        return commands.setChan(chanName, key, detect.getType(), lng, lat, length);
    }

    @Override
    public String setChanRoam(String chanName, String key, String mark, int length) {
        if (StringUtil.isBlack(chanName, key)) {
            return "chanName or key is not null";
        }
        return commands.setChanRoam(chanName, key, key, mark, length);
    }

    @Override
    public String getChans(String chanName) {
        if (StringUtil.isBlack(chanName)) {
            return "chanName is not null";
        }
        return commands.getChans(chanName);
    }

    @Override
    public String delChan(String chanName) {
        if (StringUtil.isBlack(chanName)) {
            return "chanName is not null";
        }
        return commands.delChan(chanName);
    }

    @Override
    public String pDelChans(String chanNames) {
        if (StringUtil.isBlack(chanNames)) {
            return "chanNames is not null";
        }
        return commands.pDelChans(chanNames);
    }

    @Override
    public String setHook(String hookName, String url, String key, DetectType detect, double lng, double lat, int length) {
        if (StringUtil.isBlack(hookName, url, key)) {
            return "hookName or url or key is not null";
        }
        if (detect == null) {
            return "detect is not null";
        }
        return commands.setHook(hookName, url, key, detect.getType(), lng, lat, length);
    }

    @Override
    public String setHook(String hookName, String url, String pointKey, String key, String id) {
        if (StringUtil.isBlack(hookName, url, key, pointKey, id)) {
            return "hookName or url or key is not null";
        }
        return commands.setHook(hookName, url, pointKey, key, id);
    }

    @Override
    public String setHook(String hookName, String url, String pointKey, String detect, String key, String id) {
        if (StringUtil.isBlack(hookName, url, key, detect, pointKey, id)) {
            return "hookName or url or key is not null";
        }
        return commands.setHook(hookName, url, pointKey, detect, key, id);
    }


    @Override
    public String getHook(String hookName) {
        if (StringUtil.isBlack(hookName)) {
            return "hookName is not null";
        }
        return commands.getHook(hookName);
    }

    @Override
    public String delHook(String hookName) {
        if (StringUtil.isBlack(hookName)) {
            return "hookName is not null";
        }
        return commands.delHook(hookName);
    }

    @Override
    public String pDelHook(String hookName) {
        if (StringUtil.isBlack(hookName)) {
            return "hookName is not null";
        }
        return commands.pDelHook(hookName);
    }

    @Override
    public List<List<Object>> executeBatchedCommands(@NotNull List<CommandArgs<String, String>> commandArgsList , BatchedCommandType commandType) {
        List<List<Object>> results = new ArrayList<>();
        RedisAsyncCommands<String, String> asyncCommands = client.connect().async();
        asyncCommands.setAutoFlushCommands(false);
        List<RedisFuture<List<Object>>> futureResults = new ArrayList<>();
        for (CommandArgs<String, String> commandArgs : commandArgsList) {
            RedisFuture<List<Object>> future = asyncCommands.dispatch(
                    commandType,
                    new ArrayOutput<>(StringCodec.UTF8),
                    commandArgs);
            futureResults.add(future);
        }
        asyncCommands.flushCommands();
        for (RedisFuture<List<Object>> future : futureResults) {
            try {
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        asyncCommands.setAutoFlushCommands(true);
        return results;
    }

    @Override
    public List<List<Object>> executeParallelBatchedCommands(@NotNull List<CommandArgs<String, String>> commandArgsList, BatchedCommandType commandType , int numberOfThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int chunkSize = (int) Math.ceil((double) commandArgsList.size() / numberOfThreads);
        List<CompletableFuture<List<List<Object>>>> parallelFutures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> {
                    int startIndex = i * chunkSize;
                    int endIndex = Math.min(startIndex + chunkSize, commandArgsList.size());
                    List<CommandArgs<String, String>>  subList = commandArgsList.subList(startIndex, endIndex);

                    return CompletableFuture.supplyAsync(() -> {
                        return executeBatch(subList,commandType, client.connect());
                    }, executor);
                })
                .collect(Collectors.toList());
        CompletableFuture.allOf(parallelFutures.toArray(new CompletableFuture[0])).join();
        List<List<Object>> results = parallelFutures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        executor.shutdown();
        return results;
    }

    private List<List<Object>> executeBatch(@NotNull List<CommandArgs<String, String>> commandArgsList, BatchedCommandType commandType, StatefulRedisConnection<String, String> connection) {
        RedisAsyncCommands<String, String> asyncCommands = connection.async();
        // Start pipelining
        asyncCommands.setAutoFlushCommands(false);

        // Create futures for all commands
        List<CompletableFuture<List<Object>>> futureResults = commandArgsList.stream()
                .map(commandArgs -> asyncCommands.dispatch(
                        commandType,
                        new ArrayOutput<>(StringCodec.UTF8),
                        commandArgs))
                .map(this::safeCastToCompletableFuture)
                .collect(Collectors.toList());

        // Execute the pipelined commands
        asyncCommands.flushCommands();

        // Wait for all futures to complete
        CompletableFuture.allOf(futureResults.toArray(new CompletableFuture[0])).join();

        // Collect the results
        List<List<Object>> results = futureResults.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // Restore normal behavior
        asyncCommands.setAutoFlushCommands(true);

        return results;
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<T> safeCastToCompletableFuture(RedisFuture<T> redisFuture) {
        return (CompletableFuture<T>) redisFuture;
    }

}
