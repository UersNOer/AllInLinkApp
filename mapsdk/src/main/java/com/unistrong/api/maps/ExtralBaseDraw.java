package com.unistrong.api.maps;

import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLUtils;

import com.unistrong.api.mapcore.IMapDelegate;

import java.lang.ref.SoftReference;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 自定义图层绘制辅助基类。
 */
 abstract class ExtralBaseDraw{
    SoftReference<IMapDelegate> mapDelegate =null;
    SoftReference<MapController> map =null;

    /**
     * 构造器。
     * @param mapView 地图实例。
     */
    public ExtralBaseDraw(MapView mapView){
            try{
                mapDelegate = new SoftReference<IMapDelegate>(mapView.getMapFragmentDelegate().getMapDelegate());
                map = new SoftReference<MapController>(mapView.getMap());
            }catch (Exception ex){

            }
    }

    /**
     * 绘制图片。
     * @param textureId 纹理I。
     * @param pvertices 顶点坐标缓存。
     * @param coordBuffer 映射坐标缓存。
     */
    public void drawBitmap(int textureId, FloatBuffer pvertices, FloatBuffer coordBuffer)
    {
        if ((textureId == 0) || (pvertices == null) || (coordBuffer == null)) {
            return;
        }
        GLES10.glEnable(GL10.GL_BLEND);
        GLES10.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        GLES10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GLES10.glEnable(GL10.GL_TEXTURE_2D);
        GLES10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        GLES10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        GLES10.glVertexPointer(3, GL10.GL_FLOAT, 0, pvertices);
        GLES10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
        GLES10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
        GLES10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        GLES10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        GLES10.glDisable(GL10.GL_TEXTURE_2D);
        GLES10.glDisable(GL10.GL_BLEND);

    }
    /**
     * 生成纹理ID。
     * @param gl GL10实例
     * @param texsureId 纹理ID,如果没有传0。
     * @return 纹理ID。
     */
    public int getTexsureId(GL10 gl,int texsureId)
    {
        if (texsureId == 0)
        {
            int[] arrayOfInt = { 0 };
            gl.glGenTextures(1, arrayOfInt, 0);
            texsureId = arrayOfInt[0];
        }
        return texsureId;
    }
/**
 * bind一个纹理ID。
 * @param gl GL10实例
 * @param textureID 纹理ID,如果没有传0.
 * @param bitmap 纹理
 * @return 纹理ID
 */
public static int bindTexture(GL10 gl, int textureID, Bitmap bitmap) //b
        {
        if ((bitmap == null) || (bitmap.isRecycled())) {
        return 0;
        }
        if (textureID == 0) {
        int[] arrayOfInt = {0};
        gl.glGenTextures(1, arrayOfInt, 0);
        textureID = arrayOfInt[0];
        }
        gl.glEnable(GL10.GL_TEXTURE_2D);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        return textureID;
        }
}
