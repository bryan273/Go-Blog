import ReplyTile from "../../elements/ReplyTile";
import ReplyInput from "../../elements/ReplyInput";
import NoComment from "../../elements/NoComment";

export default function CommentSection({ postId, comments, commentsSetter }) {
  return (
    <div>
      <div style={{ height: "480px" }} className="overflow-y-auto">
        {comments.length == 0 ? (
          <NoComment />
        ) : (
          comments.map((comment, index) => {
            return (
              <ReplyTile
                key={index}
                comment={comment}
                index={index}
                comments={comments}
                commentsSetter={commentsSetter}
              />
            );
          })
        )}
      </div>
      <div>
        <ReplyInput
          postId={postId}
          allComment={comments}
          allCommentSetter={commentsSetter}
        />
      </div>
    </div>
  );
}
