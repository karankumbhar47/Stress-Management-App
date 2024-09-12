package com.example.stressApp.OthersFragments;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stressApp.OthersFragments.FidgeCube.CubeRenderer;
import com.example.stressApp.R;

public class FidgetCube extends Fragment {

    private GLSurfaceView glSurfaceView;
    private CubeRenderer renderer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fidget_cube_activity, container, false);
        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        setupGLSurfaceView();
        return view;
    }

    private void setupGLSurfaceView() {
        glSurfaceView.setEGLContextClientVersion(2);
        renderer = new CubeRenderer();
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

}