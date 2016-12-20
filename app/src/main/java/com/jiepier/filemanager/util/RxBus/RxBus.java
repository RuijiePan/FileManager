package com.jiepier.filemanager.util.RxBus;

import com.jiepier.filemanager.event.CleanActionModeEvent;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxBus {

    private final Subject<Object, Object> bus;
    private Map<Object, List<Subscription>> subscriptions;

    private RxBus() {
        //非线程安全的PublishSubject包装成线程安全的SerializedSubject
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 发送事件
     *
     * @param event 事件对象
     */
    public void post(Object event) {
        if (bus.hasObservers()) {
            bus.onNext(event);
        }
    }

    /**
     * 订阅事件
     *
     * @param subscriber 订阅者
     * @param callback   事件回调
     * @param <T>        事件类型
     */
    public <T> void subscribe(Object subscriber, Callback<T> callback) {
        Class<T> eventType = RawType.getRawType(callback);
        Subscription subscription = bus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .ofType(eventType)
                .subscribe(callback);
        add(subscriber, subscription);
    }

    /**
     * 订阅事件
     *
     * @param subscriber 订阅者
     * @param eventType  事件对象
     * @param callback   事件回调
     * @param <T>        事件类型
     */
    public <T> void subscribe(Object subscriber, Class<T> eventType, Callback<T> callback) {
        Subscription subscription = bus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .ofType(eventType)
                .subscribe(callback);
        add(subscriber, subscription);
    }

    /**
     * 订阅事件
     *
     * @param eventType  事件对象
     * @param callback   事件回调
     * @param <T>        事件类型
     */
    public <T> Subscription subscribe(Class<T> eventType, Callback<T> callback) {
        return bus.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .ofType(eventType)
                .subscribe(callback);
    }

    /**
     * 订阅事件
     *
     * @param eventType 事件对象
     * @param <T>       事件类型
     * @return 特定类型的Observable
     */
    public <T> Observable<T> observe(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 添加订阅协议
     *
     * @param subscriber   订阅者
     * @param subscription 订阅协议
     */
    public synchronized void add(Object subscriber, Subscription subscription) {
        if (subscriptions == null) {
            subscriptions = new HashMap<>();
        }

        List<Subscription> list = subscriptions.get(subscriber);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(subscription);
        subscriptions.put(subscriber, list);
    }

    /**
     * 取消事件订阅
     *
     * @param subscriber 订阅者
     */
    public synchronized void unsubscribe(Object subscriber) {
        if (subscriptions == null) return;

        List<Subscription> list = subscriptions.get(subscriber);
        if (list == null || list.isEmpty()) return;

        for (Subscription subscription : list) {
            subscription.unsubscribe();
        }
        list.clear();
        subscriptions.remove(subscriber);
    }

    /**
     * 取消所有订阅
     */
    public void unsubscribeAll() {
        Set<Map.Entry<Object, List<Subscription>>> set = subscriptions.entrySet();
        for (Map.Entry<Object, List<Subscription>> entry : set) {
            List<Subscription> list = entry.getValue();
            for (Subscription subscription : list) {
                subscription.unsubscribe();
            }
            list.clear();
        }
        subscriptions = null;
    }

    private static class SingletonHolder {
        public static volatile RxBus INSTANCE = new RxBus();
    }

    public <T> Observable<T> toObservable (Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T> Observable<T> IoToUiObservable(Class<T> eventType){
        return bus.ofType(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}