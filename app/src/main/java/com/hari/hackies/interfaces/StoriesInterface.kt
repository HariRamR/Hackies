package com.hari.hackies.interfaces

import com.hari.hackies.model.CommentModel
import com.hari.hackies.model.StoryModel

interface StoriesInterface {
    fun showCommentsBottomSheetDialog(storyModel: StoryModel, pos: Int)
    fun showReplyBottomSheetDialog(commentModel: CommentModel, pos: Int)
}