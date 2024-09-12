//package com.example.stressApp.OthersFragments.FidgeCube;
//
//import android.opengl.GLES20;
//import android.opengl.GLSurfaceView;
//import android.opengl.Matrix;
//
//import com.example.stressApp.OthersFragments.Cube;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//public class MyGLRenderer implements GLSurfaceView.Renderer {
//
//    private Cube cube;
//    private final float[] rotationMatrix = new float[16];
//    private float angleX = 0f;
//    private float angleY = 0f;
//
//    @Override
//    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
//        // Set the background color
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//
//        // Enable depth testing
//        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//
//        // Initialize the cube object
//        cube = new Cube();
//    }
//
//    @Override
//    public void onDrawFrame(GL10 unused) {
//        // Clear the screen and depth buffer
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//
//        // Initialize the rotation matrix
//        Matrix.setIdentityM(rotationMatrix, 0);
//
//        // Apply rotation transformations
//        Matrix.rotateM(rotationMatrix, 0, angleX, 0f, 1f, 0f);
//        Matrix.rotateM(rotationMatrix, 0, angleY, 1f, 0f, 0f);
//
//        // Draw the cube
//        cube.draw(rotationMatrix);
//    }
//
//    @Override
//    public void onSurfaceChanged(GL10 unused, int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
//    }
//
//    // Method to set the rotation angles
//    public void setRotation(float dx, float dy) {
//        angleX += dx * 0.5f;  // Adjust the factor for sensitivity
//        angleY += dy * 0.5f;
//    }
//}
