package org.zerock.ex01.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFoodImage is a Querydsl query type for FoodImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFoodImage extends EntityPathBase<FoodImage> {

    private static final long serialVersionUID = 764739672L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFoodImage foodImage = new QFoodImage("foodImage");

    public final QCustomRecipe custom_recipe;

    public final StringPath imgName = createString("imgName");

    public final NumberPath<Long> inum = createNumber("inum", Long.class);

    public final StringPath path = createString("path");

    public final StringPath real_path = createString("real_path");

    public final StringPath uuid = createString("uuid");

    public QFoodImage(String variable) {
        this(FoodImage.class, forVariable(variable), INITS);
    }

    public QFoodImage(Path<? extends FoodImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFoodImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFoodImage(PathMetadata metadata, PathInits inits) {
        this(FoodImage.class, metadata, inits);
    }

    public QFoodImage(Class<? extends FoodImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.custom_recipe = inits.isInitialized("custom_recipe") ? new QCustomRecipe(forProperty("custom_recipe"), inits.get("custom_recipe")) : null;
    }

}

