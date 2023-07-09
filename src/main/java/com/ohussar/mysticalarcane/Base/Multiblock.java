package com.ohussar.mysticalarcane.Base;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class Multiblock {
    private int width;
    private int depth;
    private String[] structure;

    private Map<String, Block> dictionary = new HashMap<String, Block>();
    
    public Multiblock(String[] map, int width, int depth){
        this.structure = map;
        this.width = width;
        this.depth = depth;
        
    }

    public void setDictionaryKey(String key, Block block){
        dictionary.put(key, block);
    }

    public void removeDictionaryKey(String key){
        dictionary.remove(key);
    }
    public BlockPos[] getListOfBlocksPlaced(String key, Level level, BlockPos topLeftCorner){
        int count = 0;
        for(int k = 0; k < width*depth; k++){
            if(structure[k].equals(key)){
                count++;
            }
        }
        BlockPos[] list = new BlockPos[count];
        Block block = dictionary.get(key);
        int counttrack = 0;
        for(int xx = 0; xx < width; xx++){
            for(int zz = 0; zz < depth; zz++){
                if(block != null){
                    BlockPos target = new BlockPos(topLeftCorner.getX() + xx, topLeftCorner.getY(), topLeftCorner.getZ()+zz);
                    Block block2 = level.getBlockState(target).getBlock();
                    if(block2 == block){
                        list[counttrack] = target;
                        counttrack++;
                    }
                }
            }
        }
        return list;
    }
    public boolean checkIfAssembled(Level level, BlockPos topLeftCorner){
        boolean valid = true;
        for(int xx = 0; xx < width; xx++){
            for(int zz = 0; zz < depth; zz++){
                int indexInArray = xx * width + zz;
                Block block = dictionary.get(structure[indexInArray]);
                if(block != null){
                    BlockPos target = new BlockPos(topLeftCorner.getX() + xx, topLeftCorner.getY(), topLeftCorner.getZ()+zz);
                    Block block2 = level.getBlockState(target).getBlock();
                    if(block2 != block){
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

}
