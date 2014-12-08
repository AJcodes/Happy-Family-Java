package model;

import java.util.TreeSet;

import javax.swing.AbstractListModel;

public class SortedListModel extends AbstractListModel {

        private static final long serialVersionUID = 1L;
        private TreeSet model;

        public SortedListModel() {
                super();
                this.model = new TreeSet();;
        }

        @Override
        public Object getElementAt(int arg0) {
                
                return model.toArray()[arg0];
        }

        @Override
        public int getSize() {
                
                return model.size();
        }
        
        public int size() {
                return getSize();
        }

        public Object get(int index) {
                return getElementAt(index);
        
        }
        
        public void addElement(Object o) {
            if (model.add(o)) {
                fireContentsChanged(this, 0, getSize());
              }         
        }
        
        public boolean removeElement(Object o) {
            boolean removed = model.remove(o);
            if (removed) {
              fireContentsChanged(this, 0, getSize());
            }
            return removed;   
                
        }
        
        public Object remove(int index) {
                Object o = getElementAt(index);
                removeElement(o);
                return o;
        }
        
        public void clear() {
                model.clear();
                fireContentsChanged(this, 0, getSize());
        }

}