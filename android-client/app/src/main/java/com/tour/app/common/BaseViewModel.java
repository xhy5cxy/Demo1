package com.tour.app.common;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * BaseViewModel 是一个抽象的 ViewModel 基类，用于管理 RxJava 的 Disposable 资源
 * 该类自动处理 ViewModel 清除时的资源释放，避免内存泄漏
 */
public abstract class BaseViewModel extends ViewModel {

    /**
     * 用于管理多个 Disposable 资源的容器
     * 当 ViewModel 被清除时，会自动清理所有添加到此容器中的 Disposable
     */
    protected final CompositeDisposable disposables = new CompositeDisposable();

    /**
     * 将指定的 Disposable 添加到资源管理容器中
     *
     * @param disposable 需要被管理的 Disposable 资源，不能为 null
     */
    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    /**
     * 当 ViewModel 被清除时调用此方法
     * 自动清理所有已添加的 Disposable 资源，防止内存泄漏
     * 然后调用父类的清除逻辑
     */
    @Override
    protected void onCleared() {
        // 清理所有 Disposable 资源
        disposables.clear();
        super.onCleared();
    }
}
