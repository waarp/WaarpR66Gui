/**
   This file is part of GoldenGate Project (named also GoldenGate or GG).

   Copyright 2009, Frederic Bregier, and individual contributors by the @author
   tags. See the COPYRIGHT.txt in the distribution for a full listing of
   individual contributors.

   All GoldenGate Project is free software: you can redistribute it and/or 
   modify it under the terms of the GNU General Public License as published 
   by the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   GoldenGate is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with GoldenGate .  If not, see <http://www.gnu.org/licenses/>.
 */

package openr66.clientgui;

import java.awt.event.ActionEvent;
import java.util.EventObject;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The SingleFrameApplication class of the application.
 */
public class R66ClientGuiApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new R66ClientGuiView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of R66ClientGuiApp
     */
    public static R66ClientGuiApp getApplication() {
        return Application.getInstance(R66ClientGuiApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void start(String[] args) {
        launch(R66ClientGuiApp.class, args);
    }

    @Override
    public void exit(EventObject event) {
        R66ClientGui.exit();
        super.exit(event);
    }

    @Override
    public void quit(ActionEvent e) {
        R66ClientGui.exit();
        super.quit(e);
    }
}
