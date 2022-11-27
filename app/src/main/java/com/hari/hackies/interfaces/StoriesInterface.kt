package com.hari.hackies.interfaces

import com.hari.hackies.model.CommentModel

interface StoriesInterface {
    fun showCommentsBottomSheetDialog(id: Int, pos: Int)
    fun showReplyBottomSheetDialog(commentModel: CommentModel, pos: Int)
}