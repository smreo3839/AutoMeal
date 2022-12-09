package org.zerock.ex01.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomRecipe is a Querydsl query type for CustomRecipe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomRecipe extends EntityPathBase<CustomRecipe> {

    private static final long serialVersionUID = 1004498372L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomRecipe customRecipe = new QCustomRecipe("customRecipe");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final NumberPath<Integer> cr_hits = createNumber("cr_hits", Integer.class);

    public final NumberPath<Long> csRecipeId = createNumber("csRecipeId", Long.class);

    public final ListPath<CustomRecipeReply, QCustomRecipeReply> customRecipeReplies = this.<CustomRecipeReply, QCustomRecipeReply>createList("customRecipeReplies", CustomRecipeReply.class, QCustomRecipeReply.class, PathInits.DIRECT2);

    public final ListPath<FoodImage, QFoodImage> foodImages = this.<FoodImage, QFoodImage>createList("foodImages", FoodImage.class, QFoodImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> like_rate = createNumber("like_rate", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath recipe_category = createString("recipe_category");

    public final StringPath recipe_content = createString("recipe_content");

    public final StringPath recipeT_title = createString("recipeT_title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QUser writer;

    public QCustomRecipe(String variable) {
        this(CustomRecipe.class, forVariable(variable), INITS);
    }

    public QCustomRecipe(Path<? extends CustomRecipe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomRecipe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomRecipe(PathMetadata metadata, PathInits inits) {
        this(CustomRecipe.class, metadata, inits);
    }

    public QCustomRecipe(Class<? extends CustomRecipe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new QUser(forProperty("writer")) : null;
    }

}

