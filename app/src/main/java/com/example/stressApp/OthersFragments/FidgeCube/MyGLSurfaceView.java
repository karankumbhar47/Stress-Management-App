//package com.example.stressApp.OthersFragments.FidgeCube;
//
//import android.content.Context;
//import android.opengl.GLSurfaceView;
//import android.view.MotionEvent;
//
//public class MyGLSurfaceView extends GLSurfaceView {
//
//    private final MyGLRenderer renderer;
//    private float previousX;
//    private float previousY;
//
//    public MyGLSurfaceView(Context context) {
//        super(context);
//
//        // Set OpenGL ES version
//        setEGLContextClientVersion(2);
//
//        // Set the Renderer
//        renderer = new MyGLRenderer();
//        setRenderer(renderer);
//
//        // Render the view only when there's a change (on-demand rendering)
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                float dx = x - previousX;
//                float dy = y - previousY;
//
//                // Update the rotation angles based on touch movement
//                renderer.setRotation(dx, dy);
//                requestRender();
//        }
//
//        previousX = x;
//        previousY = y;
//        return true;
//    }
//}
