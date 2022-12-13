package org.zerock.ex01.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeBookMark is a Querydsl query type for RecipeBookMark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeBookMark extends EntityPathBase<RecipeBookMark> {

    private static final long serialVersionUID = 775117097L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeBookMark recipeBookMark = new QRecipeBookMark("recipeBookMark");

    public final NumberPath<Long> bmNum = createNumber("bmNum", Long.class);

    public final BooleanPath book_mark = createBoolean("book_mark");

    public final StringPath recipe_id = createString("recipe_id");

    public final StringPath recipe_thumbnail = createString("recipe_thumbnail");

    public final StringPath recipe_title = createString("recipe_title");

    public final BooleanPath recipeDone = createBoolean("recipeDone");

    public final QUser user;

    public QRecipeBookMark(String variable) {
        this(RecipeBookMark.class, forVariable(variable), INITS);
    }

    public QRecipeBookMark(Path<? extends RecipeBookMark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeBookMark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeBookMark(PathMetadata metadata, PathInits inits) {
        this(RecipeBookMark.class, metadata, inits);
    }

    public QRecipeBookMark(Class<? extends RecipeBookMark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

