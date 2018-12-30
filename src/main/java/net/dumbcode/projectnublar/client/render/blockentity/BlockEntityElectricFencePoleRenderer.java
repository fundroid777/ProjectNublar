package net.dumbcode.projectnublar.client.render.blockentity;

import com.google.common.collect.Lists;
import net.dumbcode.projectnublar.client.ModelHandler;
import net.dumbcode.projectnublar.server.ProjectNublar;
import net.dumbcode.projectnublar.server.block.BlockElectricFencePole;
import net.dumbcode.projectnublar.server.block.entity.BlockEntityElectricFencePole;
import net.dumbcode.projectnublar.server.utils.Connection;
import net.dumbcode.projectnublar.server.utils.ConnectionType;
import net.dumbcode.projectnublar.server.utils.MathUtils;
import net.dumbcode.projectnublar.client.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.List;

public class BlockEntityElectricFencePoleRenderer extends TileEntitySpecialRenderer<BlockEntityElectricFencePole> {


    public static final boolean useVbo = true;

    public BlockEntityElectricFencePoleRenderer() {

        BufferBuilder buff = Tessellator.getInstance().getBuffer();

        for (ConnectionType type : ConnectionType.values()) {
            if(useVbo) {
                type.vbo = new VertexBuffer(DefaultVertexFormats.POSITION_TEX_NORMAL);
            } else {
                GlStateManager.glNewList(type.listID = GlStateManager.glGenLists(1), GL11.GL_COMPILE);
            }
            buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
            IBakedModel model;
            switch (type) {
                case HIGH_SECURITY:
                    model = ModelHandler.HIGH_SECURITY;
                    break;
                case LIGHT_STEEL:
                default:
                    model = ModelHandler.LIGHT_STEEL;
                    break;
            }
            for (BakedQuad quad : model.getQuads(Blocks.STONE.getDefaultState(), null, 0L)) {
                buff.addVertexData(quad.getVertexData());
            }
            if(useVbo) {
                buff.finishDrawing();
                buff.reset();
                type.vbo.bufferData(buff.getByteBuffer());
            } else {
                Tessellator.getInstance().draw();
                GlStateManager.glEndList();
            }
        }
    }

    @Override
    public void render(BlockEntityElectricFencePole te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
//        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(x, y, z);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();

        GlStateManager.color(1f, 1f, 1f, 1f);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ProjectNublar.MODID, "textures/blocks/electric_fence.png"));
        for (Connection connection : te.fenceConnections) {
            BlockEntityElectricFenceRenderer.renderConnection(connection);
        }
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.color(1f,1f,1f,1f);
        GlStateManager.enableAlpha();
        BlockPos pos = te.getPos();
        IBlockState state = te.getWorld().getBlockState(pos);
        Block block = state.getBlock();
        BufferBuilder buff = Tessellator.getInstance().getBuffer();
        if (block instanceof BlockElectricFencePole && state.getValue(BlockElectricFencePole.INDEX_PROPERTY) == 0) {
            ConnectionType type = ((BlockElectricFencePole) block).getType();
            Minecraft.getMinecraft().renderEngine.bindTexture(type.getTexture());
            GlStateManager.pushMatrix();
            float rotation = 90F; //Expensive calls ahead. Maybe try and cache them?
            if(!te.fenceConnections.isEmpty()) {

                List<Connection> differingConnections = Lists.newArrayList();
                for (Connection connection : te.fenceConnections) {
                    boolean has = false;
                    for (Connection dc : differingConnections) {
                        if(connection.getFrom().equals(dc.getFrom()) && connection.getTo().equals(dc.getTo())) {
                            has = true;
                            break;
                        }
                    }
                    if(!has) {
                        differingConnections.add(connection);
                    }
                }

                if (differingConnections.size() == 1) {
                    Connection connection = differingConnections.get(0);
                    double[] in = connection.getCache().getIn();
                    rotation = (float) Math.toDegrees(Math.atan((in[2] - in[3]) / (in[1] - in[0])));
                } else {

                    Connection connection1 = differingConnections.get(0);
                    Connection connection2 = differingConnections.get(1);

                    double[] in1 = connection1.getCache().getIn();
                    double[] in2 = connection2.getCache().getIn();

                    double angle1 = MathUtils.horizontalDegree(in1[1] - in1[0], in1[2] - in1[3], connection1.getPosition().equals(connection1.getMin()));
                    double angle2 = MathUtils.horizontalDegree(in2[1] - in2[0], in2[2] - in2[3], connection2.getPosition().equals(connection2.getMin()));

                    rotation = (float) (angle1 + (angle2-angle1)/2D) + 90F;

                }
            }

            rotation += ((BlockElectricFencePole) block).getType().getRotationOffset() + 90F;
            if(te.rotatedAround) {
                rotation += 180;
            }
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            GlStateManager.rotate(rotation, 0, 1, 0);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            if(useVbo) {
                type.vbo.bindBuffer();

                RenderUtils.setupPointers(DefaultVertexFormats.POSITION_TEX_NORMAL);

                RenderHelper.enableStandardItemLighting();

                type.vbo.drawArrays(GL11.GL_QUADS);
                type.vbo.unbindBuffer();

                RenderUtils.disableStates(DefaultVertexFormats.POSITION_TEX_NORMAL);
            } else {
                GlStateManager.callList(type.listID);
            }

            GlStateManager.popMatrix();

        }
        buff.setTranslation(0, 0, 0);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(BlockEntityElectricFencePole te) {
        return true;
    }
}
