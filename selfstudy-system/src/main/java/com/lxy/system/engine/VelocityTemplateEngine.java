package com.lxy.system.engine;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

public class VelocityTemplateEngine extends AbstractTemplateEngine {
    private VelocityEngine velocityEngine;

    public VelocityTemplateEngine() {
        try {
            Class.forName("org.apache.velocity.util.DuckType");
        } catch (ClassNotFoundException var2) {
            this.LOGGER.warn("Velocity 1.x is outdated, please upgrade to 2.x or later.");
        }

    }

    public @NotNull VelocityTemplateEngine init(@NotNull ConfigBuilder configBuilder) {
        if (null == this.velocityEngine) {
            Properties p = new Properties();
            p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            p.setProperty("resource.loader.file.path", "");
            p.setProperty("UTF-8", ConstVal.UTF8);
            p.setProperty("resource.default_encoding", ConstVal.UTF8);
            p.setProperty("resource.loader.file.unicode", "true");
            this.velocityEngine = new VelocityEngine(p);
        }

        return this;
    }

    public void writer(@NotNull Map<String, Object> objectMap, @NotNull String templatePath, @NotNull File outputFile) throws Exception {
        Template template = this.velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        FileOutputStream fos = new FileOutputStream(outputFile);

        try {
            OutputStreamWriter ow = new OutputStreamWriter(fos, ConstVal.UTF8);

            try {
                BufferedWriter writer = new BufferedWriter(ow);

                try {
                    template.merge(new VelocityContext(objectMap), writer);
                } catch (Throwable var13) {
                    try {
                        writer.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }

                    throw var13;
                }

                writer.close();
            } catch (Throwable var14) {
                try {
                    ow.close();
                } catch (Throwable var11) {
                    var14.addSuppressed(var11);
                }

                throw var14;
            }

            ow.close();
        } catch (Throwable var15) {
            try {
                fos.close();
            } catch (Throwable var10) {
                var15.addSuppressed(var10);
            }

            throw var15;
        }

        fos.close();
        this.LOGGER.info("模板:{};  文件:{}", templatePath, outputFile);
    }

    public @NotNull String templateFilePath(@NotNull String filePath) {
        String dotVm = ".vm";
        return filePath.endsWith(".vm") ? filePath : filePath + ".vm";
    }
}
