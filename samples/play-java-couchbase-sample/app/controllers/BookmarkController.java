/**
 * 
 */
package controllers;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import models.Bookmark;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author valore
 *
 */
public class BookmarkController extends Controller {
	
	public static Form<Bookmark> bookmarkForm = Form.form(Bookmark.class);
	
	public static Result save() {
		Bookmark bookmark = bookmarkForm.bindFromRequest().get();
		bookmark.id = UUID.randomUUID().toString();
		bookmark.timeStamp = new Date().toString();
		Bookmark.save(bookmark);
		return redirect(routes.BookmarkController.list());
	}
	
	public static Result get(String id) {
		Bookmark bookmark = Bookmark.get(id);
		return ok(views.html.bookmark.bookmark.render(bookmark));
	}
	
	public static Result list() {
		Collection<Bookmark> bookmarks = Bookmark.list();
		return ok(views.html.bookmark.list.render(bookmarkForm, bookmarks));
	}

	public static Result edit(String id) {
		Bookmark bookmark = Bookmark.get(id);
		return ok(views.html.bookmark.edit.render(bookmarkForm.fill(bookmark)));
	}
	
	public static Result update() {
		Bookmark bookmark = bookmarkForm.bindFromRequest().get();
		Bookmark.update(bookmark);
		return redirect(routes.BookmarkController.list());
	}
	
	public static Result delete(String id) {
		Bookmark.delete(id);
		return redirect(routes.BookmarkController.list());
	}

}
