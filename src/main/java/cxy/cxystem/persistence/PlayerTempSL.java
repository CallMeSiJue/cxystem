package cxy.cxystem.persistence;

import cxy.cxystem.CxysTem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class PlayerTempSL extends PersistentState {
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        return nbt;
    }
    public static PlayerTempSL createFromNbt(NbtCompound tag) {
        PlayerTempSL state = new PlayerTempSL();
        //state.totalDirtBlocksBroken = tag.getInt("totalDirtBlocksBroken");
        return state;
    }

    private static Type<PlayerTempSL> type = new Type<>(
            PlayerTempSL::new, // 若不存在 'StateSaverAndLoader' 则创建
            PlayerTempSL::createFromNbt, // 若存在 'StateSaverAndLoader' NBT, 则调用 'createFromNbt' 传入参数
            null // 此处理论上应为 'DataFixTypes' 的枚举，但我们直接传递为空(null)也可以
    );

    public static PlayerTempSL getServerState(MinecraftServer server) {
        // (注：如需在任意维度生效，请使用 'World.OVERWORLD' ，不要使用 'World.END' 或 'World.NETHER')
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // 当第一次调用了方法 'getOrCreate' 后，它会创建新的 'StateSaverAndLoader' 并将其存储于  'PersistentStateManager' 中。
        //  'getOrCreate' 的后续调用将本地的 'StateSaverAndLoader' NBT 传递给 'StateSaverAndLoader::createFromNbt'。
        PlayerTempSL state = persistentStateManager.getOrCreate(type, CxysTem.MOD_ID);

        // 若状态未标记为脏(dirty)，当 Minecraft 关闭时， 'writeNbt' 不会被调用，相应地，没有数据会被保存。
        // 从技术上讲，只有在事实上发生数据变更时才应当将状态标记为脏(dirty)。
        // 但大多数开发者和模组作者会对他们的数据未能保存而感到困惑，所以不妨直接使用 'markDirty' 。
        // 另外，这只将对应的布尔值设定为 TRUE，代价是文件写入磁盘时模组的状态不会有任何改变。(这种情况非常少见)
        state.markDirty();

        return state;
    }
}
