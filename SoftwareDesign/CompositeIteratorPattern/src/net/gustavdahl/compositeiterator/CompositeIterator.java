package net.gustavdahl.compositeiterator;

public class CompositeIterator implements Iterator
{
	private Iterator _baseIterator; // remember where you came from
	private Iterator _currentIterator;

	public CompositeIterator(Iterator partsIterator)
	{
		_currentIterator = partsIterator;
		_baseIterator = partsIterator;
	}

	public boolean hasNext()
	{
		if (_currentIterator.hasNext())
		{
			//System.out.println(_currentIterator.current().getName() +" has next: ");
			return true;
		}

		if (_currentIterator == _baseIterator)
		{
			//System.out.println(_currentIterator.current().getName()  +" has next: FALSE");
			return false;
		}

		_currentIterator = _baseIterator;
		return _currentIterator.hasNext();
	}

	public Component next()
	{
		if (!hasNext())
			return null;

		Component component = _currentIterator.next();
		if (_currentIterator == _baseIterator)
			_currentIterator = component.Iterator();
		return component;
	}

	@Override
	public Component current()
	{
		return _currentIterator.current();
	}
}
