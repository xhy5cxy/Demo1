package com.tour.app.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tour.app.auth.repository.AuthRepository;
import com.tour.app.auth.viewmodel.AuthViewModel;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {
    
    private final AuthRepository authRepository;
    
    @Inject
    public ViewModelFactory(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(authRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
} 