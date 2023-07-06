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

    private int number = 0;
    private double radius = 0;

    public static enum CUSTOM_FUNCTIONS {
        normal,
        randomCircle,
    }
    private final CUSTOM_FUNCTIONS custom;
    public SpawnParticles(Vec3 pos, Vec3 spd, ParticleOptions particle, CUSTOM_FUNCTIONS custom){
        this.pos = pos;
        this.spd = spd;
        this.particle = particle;
        this.custom = custom;
    }
    public SpawnParticles(FriendlyByteBuf buf){
        this.pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.spd = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.particle = fromInt(buf.readInt());
        this.custom = buf.readEnum(CUSTOM_FUNCTIONS.class);
        if(custom == CUSTOM_FUNCTIONS.randomCircle){
            this.number = buf.readInt();
            this.radius = buf.readDouble();
        }
    }

    public SpawnParticles(ParticleOptions particle, Vec3 pos, int n, double radius){
        this.pos = pos;
        this.spd = new Vec3(0, 0, 0);
        this.particle = particle;
        this.custom = CUSTOM_FUNCTIONS.randomCircle;
        this.radius = radius;
        this.number = n;
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
                    Vec3 spd = new Vec3(xx, 0, zz).scale(0.125f);
                    level.addParticle(particle, p.x, p.y, p.z, spd.x, spd.y, spd.z);
                }
            break;
            case normal:
                level.addParticle(particle, pos.x, pos.y, pos.z, spd.x, spd.y, spd.z);
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

        buf.writeInt(toInt(particle));   
        buf.writeEnum(custom);
        if(custom == CUSTOM_FUNCTIONS.randomCircle){
            buf.writeInt(number);
            buf.writeDouble(radius);
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
