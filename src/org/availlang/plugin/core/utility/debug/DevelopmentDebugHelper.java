package org.availlang.plugin.core.utility.debug;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import java.awt.*;

/**
 * A {@code DevelopmentDebugHelper} is a class that carries static methods that
 * make it easier to introduce quick debug helpers to display data.
 *
 * @author Richard Arriaga &lt;rich@availlang.org&gt;
 */
public class DevelopmentDebugHelper
{
	/**
	 * Launch a {@link JBPopup} with the provided text and write that same text
	 * to standard out.
	 *
	 * @param text
	 *        The {@code String} to display.
	 */
	public static void debugTextPopup (final String text)
	{
		System.out.println("root string: " + text);
		JBPopupFactory jbPopupFactory = JBPopupFactory.getInstance();
		JBPopup message = jbPopupFactory.createMessage("root string: " + text);
		message.show(new RelativePoint(new Point(100, 100)));
	}

}
