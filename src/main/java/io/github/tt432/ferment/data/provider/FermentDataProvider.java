package io.github.tt432.ferment.data.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import lombok.RequiredArgsConstructor;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author TT432
 */
@RequiredArgsConstructor
public abstract class FermentDataProvider implements DataProvider {
    private final PackOutput.Target target;
    private final PackOutput output;
    private final String modid;
    private final String folder;
    private final Map<Codec<?>, Map<String, Object>> data = new HashMap<>();

    @SuppressWarnings("unchecked")
    private <T> T cast(Map.Entry<String, Object> entry) {
        return (T) entry.getValue();
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        addAll();

        if (!data.isEmpty()) {
            var path = this.output.getOutputFolder(target).resolve(this.modid).resolve(folder);
            return CompletableFuture.allOf(data.entrySet().stream().flatMap(e ->
                    e.getValue().entrySet().stream().map(e2 ->
                            DataProvider.saveStable(cache, e.getKey().encodeStart(JsonOps.INSTANCE, cast(e2)).getOrThrow(),
                                    path.resolve(e2.getKey() + ".json")))).toArray(CompletableFuture<?>[]::new));
        }

        return CompletableFuture.allOf();
    }

    protected abstract void addAll();

    protected <T> void add(Codec<T> codec, String id, T value) {
        data.computeIfAbsent(codec, k -> new HashMap<>()).put(id, value);
    }

    @Override
    public @NotNull String getName() {
        return this.getClass().getSimpleName();
    }
}
