package org.zerock.ex01.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1739138512L;

    public static final QUser user = new QUser("user");

    public final StringPath address = createString("address");

    public final StringPath diet = createString("diet");

    public final StringPath img = createString("img");

    public final StringPath nick_name = createString("nick_name");

    public final StringPath phone_num = createString("phone_num");

    public final ListPath<RecipeBookMark, QRecipeBookMark> recipeBookMarks = this.<RecipeBookMark, QRecipeBookMark>createList("recipeBookMarks", RecipeBookMark.class, QRecipeBookMark.class, PathInits.DIRECT2);

    public final StringPath token = createString("token");

    public final StringPath user_email = createString("user_email");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

