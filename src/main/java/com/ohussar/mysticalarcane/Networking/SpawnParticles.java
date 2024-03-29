package com.ohussar.mysticalarcane.Networking;
import java.util.function.Supplier;

import com.ohussar.mysticalarcane.Base.ModParticles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class SpawnParticles {
    private final Vec3 pos;
    private final Vec3 spd;
    private final ParticleOptions particle;
    private final double velmultiplier;
    private Vec3 endpos = new Vec3(0, 0, 0);
    private int number = 0;
    private double radius = 0;

    public static enum CUSTOM_FUNCTIONS {
        normal,
        randomCircle,
        alongLine,
    }
    private final CUSTOM_FUNCTIONS custom;
    /** 
     normal constructor
    */
    public SpawnParticles(Vec3 pos, Vec3 spd, ParticleOptions particle, CUSTOM_FUNCTIONS custom){
        this.pos = pos;
        this.spd = spd;
        this.particle = particle;
        this.custom = custom;
        this.velmultiplier = 1;
    }
    public SpawnParticles(FriendlyByteBuf buf){
        this.pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.spd = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.velmultiplier = buf.readDouble();
        this.particle = fromInt(buf.readInt());
        this.custom = buf.readEnum(CUSTOM_FUNCTIONS.class);
        if(custom == CUSTOM_FUNCTIONS.randomCircle){
            this.number = buf.readInt();
            this.radius = buf.readDouble();
        }
        if(custom == CUSTOM_FUNCTIONS.alongLine){
            this.number = buf.readInt();
            this.endpos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }
    /** 
     random circle constructor
    */
    public SpawnParticles(ParticleOptions particle, Vec3 pos, int n, double radius, double velmult){
        this.pos = pos;
        this.spd = new Vec3(0, 0, 0);
        this.particle = particle;
        this.custom = CUSTOM_FUNCTIONS.randomCircle;
        this.radius = radius;
        this.number = n;
        this.velmultiplier = velmult;
    }
    /** 
     along line constructor
    */
    public SpawnParticles(ParticleOptions particle, Vec3 pos, Vec3 endpos, int n, double velmult){
        this.pos = pos;
        this.spd = new Vec3(0,0,0);
        this.particle = particle;
        this.endpos = endpos;
        this.number = n;
        this.custom = CUSTOM_FUNCTIONS.alongLine;
        this.velmultiplier = velmult;
    }

    public int toInt(ParticleOptions p){
        if(p == ParticleTypes.FIREWORK) {return 0;}
        if(p == ParticleTypes.ASH)      {return 1;}
        if(p == ParticleTypes.EXPLOSION){return 2;}
        if(p == ParticleTypes.NOTE)     {return 3;}
        if(p == ParticleTypes.CRIT)     {return 4;}
        if(p == ModParticles.MANA_PARTICLE.get()) {return 5;}
        return -1;
    }
    public ParticleOptions fromInt(int p){
        if(p == 0) {return ParticleTypes.FIREWORK;}
        if(p == 1) {return ParticleTypes.ASH;}
        if(p == 2) {return ParticleTypes.EXPLOSION;}
        if(p == 3) {return ParticleTypes.NOTE;}
        if(p == 4) {return ParticleTypes.CRIT;}
        if(p == 5) {return ModParticles.MANA_PARTICLE.get();}     
        return ParticleTypes.CRIT;   
    }

    public void callCustomFunction(CUSTOM_FUNCTIONS func, ClientLevel level){
        switch(func){
            case randomCircle:
                for(int i = 0; i < number; i++){
                    double xx = Math.cos(2*Math.PI/number * i);
                    double zz = Math.sin(2*Math.PI/number * i);
                    Vec3 p = new Vec3(xx * radius + pos.x, pos.y,  zz* radius + pos.z);
                    Vec3 spd = new Vec3(xx, 0, zz).scale(0.125f).scale(velmultiplier);
                    level.addParticle(particle, p.x, p.y, p.z, spd.x, spd.y, spd.z);
                }
            break;
            case normal:
                level.addParticle(particle, pos.x, pos.y, pos.z, spd.x, spd.y, spd.z);
            break;
            case alongLine:
                for(int i = 0; i < number; i++){
                    double xx = Math.random() * 0.125;
                    double yy = Math.random() * 0.125;
                    double zz = Math.random() * 0.125;

                    double distribution = Math.random();
                    distribution = Math.min(0.75, distribution);
                    double dist = pos.distanceTo(endpos);

                    Vec3 angle = endpos.subtract(pos).normalize();

                    level.addParticle(particle, pos.x + angle.x*dist*distribution + xx, 
                                                pos.y + angle.y*dist*distribution + yy, 
                                                pos.z + angle.z*dist*distribution + zz, 
                                                (angle.x + xx) * velmultiplier, (angle.y + yy)*velmultiplier, (angle.z + zz)*velmultiplier);

                }
            break;
        }
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);

        buf.writeDouble(spd.x);
        buf.writeDouble(spd.y);
        buf.writeDouble(spd.z);
        buf.writeDouble(velmultiplier);
        buf.writeInt(toInt(particle));   
        buf.writeEnum(custom);
        if(custom == CUSTOM_FUNCTIONS.randomCircle){
            buf.writeInt(number);
            buf.writeDouble(radius);
        }
        if(custom == CUSTOM_FUNCTIONS.alongLine){
            buf.writeInt(number);
            buf.writeDouble(endpos.x);
            buf.writeDouble(endpos.y);
            buf.writeDouble(endpos.z);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft mine = Minecraft.getInstance();
            callCustomFunction(custom, mine.level);
        });
        return true;
    }

}
