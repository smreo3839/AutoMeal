package org.zerock.ex01.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomRecipeReply is a Querydsl query type for CustomRecipeReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomRecipeReply extends EntityPathBase<CustomRecipeReply> {

    private static final long serialVersionUID = -1427787898L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomRecipeReply customRecipeReply = new QCustomRecipeReply("customRecipeReply");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QCustomRecipe customRecipe;

    public final StringPath imgUrl = createString("imgUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QUser replyer;

    public final StringPath rp_content = createString("rp_content");

    public final NumberPath<Long> rp_num = createNumber("rp_num", Long.class);

    public QCustomRecipeReply(String variable) {
        this(CustomRecipeReply.class, forVariable(variable), INITS);
    }

    public QCustomRecipeReply(Path<? extends CustomRecipeReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomRecipeReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomRecipeReply(PathMetadata metadata, PathInits inits) {
        this(CustomRecipeReply.class, metadata, inits);
    }

    public QCustomRecipeReply(Class<? extends CustomRecipeReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customRecipe = inits.isInitialized("customRecipe") ? new QCustomRecipe(forProperty("customRecipe"), inits.get("customRecipe")) : null;
        this.replyer = inits.isInitialized("replyer") ? new QUser(forProperty("replyer")) : null;
    }

}

